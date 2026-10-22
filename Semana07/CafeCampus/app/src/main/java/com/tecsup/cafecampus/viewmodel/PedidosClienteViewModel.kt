package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class PedidosClienteViewModel(private val repo: CafeRepository) : ViewModel() {

    private val clienteId = MutableStateFlow<Int?>(null)
    fun setCliente(id: Int) { clienteId.value = id }

    val misPedidos: StateFlow<List<PedidoConDetalle>> = clienteId
        .flatMapLatest { id -> if (id == null) flowOf(emptyList()) else repo.observarPedidosCliente(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Activos = RECIBIDO, PREPARANDO, LISTO. */
    val activos: StateFlow<List<PedidoConDetalle>> = misPedidos
        .map { l -> l.filter { EstadoPedido.desde(it.pedido.estado).esActivo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val historial: StateFlow<List<PedidoConDetalle>> = misPedidos
        .map { l -> l.filterNot { EstadoPedido.desde(it.pedido.estado).esActivo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Un StateFlow por pedido, reutilizado mientras el ViewModel viva.
    private val seguimientos = mutableMapOf<Int, StateFlow<PedidoConDetalle?>>()
    fun pedido(id: Int): StateFlow<PedidoConDetalle?> = seguimientos.getOrPut(id) {
        repo.observarPedido(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    private val _enviando = MutableStateFlow(false)
    val enviando: StateFlow<Boolean> = _enviando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Evento de una sola vez: id del pedido creado (la pantalla navega al Seguimiento).
    private val _creado = Channel<Long>(Channel.BUFFERED)
    val pedidoCreado: Flow<Long> = _creado.receiveAsFlow()

    /**
     * "Ahora" más los 3 próximos horarios de media hora, con al menos 10 minutos de margen.
     * Ejemplo: a las 10:05 devuelve [Ahora, 10:30, 11:00, 11:30].
     */
    fun opcionesHora(ahora: LocalTime = LocalTime.now()): List<String> {
        val minutosConMargen = ahora.hour * 60 + ahora.minute + 10
        val primero = ((minutosConMargen + 29) / 30) * 30
        val franjas = (0..2)
            .map { primero + it * 30 }
            .filter { it < 24 * 60 }
            .map { String.format(Locale.US, "%02d:%02d", it / 60, it % 60) }
        return listOf("Ahora") + franjas
    }

    fun confirmar(items: List<ItemCarrito>, horaRecojo: String, pago: String, nota: String) {
        val cid = clienteId.value
        if (cid == null) { _error.value = "No hay una sesión activa"; return }
        if (_enviando.value) return // evita pedidos duplicados por doble toque
        _enviando.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                _creado.send(repo.crearPedido(cid, items, horaRecojo, pago, nota))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = e.message ?: "No se pudo crear el pedido"
            } finally {
                _enviando.value = false
            }
        }
    }

    fun limpiarError() { _error.value = null }
}