package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Resultado de volver a cargar un pedido anterior en el carrito. */
data class ResultadoRepetir(val agregados: Int, val omitidos: Int)

class MenuViewModel(private val repo: CafeRepository) : ViewModel() {

    // Flow (Room) -> StateFlow con stateIn: la View solo observa
    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ---- Filtro por categoría ----
    private val _categoria = MutableStateFlow<String?>(null)
    val categoria: StateFlow<String?> = _categoria.asStateFlow()

    val categorias: StateFlow<List<String>> = productos
        .map { lista -> lista.map { it.categoria }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun elegirCategoria(c: String?) { _categoria.value = c }

    // ---- Búsqueda por nombre ----
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    fun buscar(texto: String) { _busqueda.value = texto }

    /** Productos que cumplen a la vez la categoría elegida y el texto buscado. La lógica vive aquí, no en la View. */
    val productosVisibles: StateFlow<List<Producto>> =
        combine(productos, _categoria, _busqueda) { lista, cat, texto ->
            val q = texto.trim()
            lista.filter { p ->
                (cat == null || p.categoria == cat) &&
                        (q.isEmpty() || p.nombre.contains(q, ignoreCase = true))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ---- Carrito (en memoria; sobrevive a rotaciones porque vive en el ViewModel) ----
    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    val cantidadItems: StateFlow<Int> = _carrito
        .map { lista -> lista.sumOf { it.cantidad } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Si el producto ya está en el carrito suma la cantidad en vez de duplicar la línea. */
    fun agregar(producto: Producto, cantidad: Int, nota: String = "") {
        if (!producto.disponible || cantidad < 1) return
        _carrito.update { lista ->
            if (lista.none { it.producto.id == producto.id }) {
                lista + ItemCarrito(producto, cantidad, nota)
            } else {
                lista.map {
                    if (it.producto.id == producto.id)
                        it.copy(cantidad = it.cantidad + cantidad, nota = nota.ifBlank { it.nota })
                    else it
                }
            }
        }
    }

    fun quitar(productoId: Int) { _carrito.update { lista -> lista.filter { it.producto.id != productoId } } }

    fun vaciar() { _carrito.value = emptyList() }

    fun total(): Double = _carrito.value.sumOf { it.subtotal }

    // ---- Repetir pedido ----
    // Evento de una sola vez: la navegación lo recibe y decide qué mostrar.
    private val _repeticion = Channel<ResultadoRepetir>(Channel.BUFFERED)
    val repeticion: Flow<ResultadoRepetir> = _repeticion.receiveAsFlow()

    /**
     * Vuelve a cargar al carrito las líneas de un pedido anterior, con el precio ACTUAL del menú.
     * Los productos que ya no existen o están agotados se omiten y se informa cuántos fueron.
     */
    fun repetirPedido(detalles: List<DetallePedido>) {
        viewModelScope.launch {
            val actuales = repo.observarProductos().first().associateBy { it.id }
            var agregados = 0
            var omitidos = 0
            detalles.forEach { d ->
                val producto = actuales[d.productoId]
                if (producto != null && producto.disponible) {
                    agregar(producto, d.cantidad)
                    agregados++
                } else {
                    omitidos++
                }
            }
            _repeticion.send(ResultadoRepetir(agregados, omitidos))
        }
    }
}