package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuAdminViewModel(private val repo: CafeRepository) : ViewModel() {

    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ---- Filtro por categoría (lista del administrador) ----
    private val _categoria = MutableStateFlow<String?>(null)

    /** Categorías que tienen al menos un producto, en el mismo orden que el menú del estudiante. */
    val categorias: StateFlow<List<String>> = productos
        .map { lista -> ordenar(lista.map { it.categoria }.distinct()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Categoría elegida; vuelve a "Todo" (null) si ya no queda ningún producto en ella. */
    val categoria: StateFlow<String?> = combine(productos, _categoria) { lista, elegida ->
        elegida?.takeIf { c -> lista.any { it.categoria == c } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val productosVisibles: StateFlow<List<Producto>> = combine(productos, categoria) { lista, cat ->
        if (cat == null) lista else lista.filter { it.categoria == cat }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun elegirCategoria(c: String?) { _categoria.value = c }

    /** Opciones del formulario: las categorías base más las que ya existen en el menú. */
    val categoriasDisponibles: StateFlow<List<String>> = productos
        .map { lista -> ordenar((CATEGORIAS_BASE + lista.map { it.categoria }).distinct()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CATEGORIAS_BASE)

    /** Nuevo (id = 0) o editado (id existente). */
    fun guardar(p: Producto) { viewModelScope.launch { repo.guardarProducto(p) } }

    fun eliminar(p: Producto) { viewModelScope.launch { repo.eliminarProducto(p) } }

    fun alternarDisponibilidad(p: Producto) {
        viewModelScope.launch { repo.cambiarDisponibilidad(p.id, !p.disponible) }
    }

    /** Primero las categorías de ORDEN_CATEGORIAS, luego el resto alfabéticamente. */
    private fun ordenar(cats: List<String>): List<String> = cats.sortedWith(
        compareBy<String> { cat ->
            ORDEN_CATEGORIAS.indexOf(cat).let { i -> if (i >= 0) i else Int.MAX_VALUE }
        }.thenBy { it }
    )

    private companion object {
        val ORDEN_CATEGORIAS = listOf("Desayunos", "Almuerzos", "Bebidas")
        val CATEGORIAS_BASE = listOf("Desayunos", "Almuerzos", "Bebidas", "Snacks")
    }
}