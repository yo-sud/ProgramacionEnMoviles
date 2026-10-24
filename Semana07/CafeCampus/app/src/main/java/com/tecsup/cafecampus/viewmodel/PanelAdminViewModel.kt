package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.PedidosPorHora
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class PanelAdminViewModel(private val repo: CafeRepository) : ViewModel() {

    private fun <T> Flow<T>.estado(inicial: T): StateFlow<T> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), inicial)

    val nuevos: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO).estado(0)
    val preparando: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.PREPARANDO).estado(0)
    val listos: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.LISTO).estado(0)

    /** Inicio del día actual; se recalcula cada minuto para que "ventas de hoy" cambie a medianoche. */
    private val inicioDia: Flow<Long> = flow {
        while (true) {
            emit(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
            delay(60_000)
        }
    }.distinctUntilChanged()

    val ventasHoy: StateFlow<Double> = inicioDia
        .flatMapLatest { repo.observarVentasHoy(it) }
        .estado(0.0)

    /** Pedidos de hoy por hora, con ceros en las horas intermedias. La consulta viene de Room. */
    val pedidosPorHora: StateFlow<List<PedidosPorHora>> = inicioDia
        .flatMapLatest { repo.observarPedidosPorHora(it) }
        .map { completarHoras(it) }
        .estado(emptyList())

    /** Total de pedidos de hoy (sin cancelados): es la suma de las barras del gráfico. */
    val pedidosHoy: StateFlow<Int> = pedidosPorHora
        .map { lista -> lista.sumOf { it.cantidad } }
        .estado(0)

    val nuevosPedidos: StateFlow<List<PedidoConDetalle>> =
        repo.observarPedidosPorEstado(EstadoPedido.RECIBIDO).estado(emptyList())

    fun preparar(p: PedidoConDetalle) {
        viewModelScope.launch { repo.cambiarEstado(p.pedido.id, EstadoPedido.PREPARANDO) }
    }

    /** Horas contiguas que cubren todos los pedidos de hoy, con un mínimo de 5 barras. */
    private fun completarHoras(datos: List<PedidosPorHora>): List<PedidosPorHora> {
        if (datos.isEmpty()) return emptyList()
        val porHora = datos.associate { it.hora to it.cantidad }
        val primera = datos.minOf { it.hora }
        val ultima = datos.maxOf { it.hora }
        val hasta = maxOf(ultima, primera + 4).coerceAtMost(23)
        val desde = minOf(primera, hasta - 4).coerceAtLeast(0)
        return (desde..hasta).map { PedidosPorHora(it, porHora[it] ?: 0) }
    }
}