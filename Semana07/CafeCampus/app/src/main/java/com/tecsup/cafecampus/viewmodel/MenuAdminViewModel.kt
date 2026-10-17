package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuAdminViewModel(private val repo: CafeRepository) : ViewModel() {

    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Nuevo (id = 0) o editado (id existente). */
    fun guardar(p: Producto) { viewModelScope.launch { repo.guardarProducto(p) } }

    fun eliminar(p: Producto) { viewModelScope.launch { repo.eliminarProducto(p) } }

    fun alternarDisponibilidad(p: Producto) {
        viewModelScope.launch { repo.cambiarDisponibilidad(p.id, !p.disponible) }
    }
}