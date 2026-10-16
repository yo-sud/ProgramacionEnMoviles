package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.EstadoPedido
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PedidosAdminViewModel(private val repo: CafeRepository) : ViewModel() {

    val estadoSeleccionado = MutableStateFlow(EstadoPedido.RECIBIDO)
    fun seleccionar(e: EstadoPedido) { estadoSeleccionado.value = e }

    /** Cambia según el chip elegido. */
    val pedidos: StateFlow<List<PedidoConDetalle>> = estadoSeleccionado
        .flatMapLatest { repo.observarPedidosPorEstado(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val detalles = mutableMapOf<Int, StateFlow<PedidoConDetalle?>>()
    fun pedido(id: Int): StateFlow<PedidoConDetalle?> = detalles.getOrPut(id) {
        repo.observarPedido(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    /** RECIBIDO -> PREPARANDO -> LISTO -> ENTREGADO (la regla vive en EstadoPedido.siguiente()). */
    fun avanzar(p: PedidoConDetalle) {
        val sig = EstadoPedido.desde(p.pedido.estado).siguiente() ?: return
        viewModelScope.launch { repo.cambiarEstado(p.pedido.id, sig) }
    }

    fun cancelar(p: PedidoConDetalle) {
        if (!EstadoPedido.desde(p.pedido.estado).esActivo) return
        viewModelScope.launch { repo.cambiarEstado(p.pedido.id, EstadoPedido.CANCELADO) }
    }

    fun cambiarA(p: PedidoConDetalle, estado: EstadoPedido) {
        viewModelScope.launch { repo.cambiarEstado(p.pedido.id, estado) }
    }
}