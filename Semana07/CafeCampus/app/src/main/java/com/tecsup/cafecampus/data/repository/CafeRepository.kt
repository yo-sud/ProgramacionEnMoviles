package com.tecsup.cafecampus.data.repository

import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.ItemCarrito
import com.tecsup.cafecampus.model.PedidosPorHora
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de datos de la app. Los ViewModels solo conocen esta interface:
 * hoy la implementa Room; mañana puede implementarla un servicio web sin tocar pantallas.
 * Regla: lectura que actualiza la pantalla -> Flow (sin suspend). Escritura/acción única -> suspend.
 */
interface CafeRepository {
    // Productos (ejemplo resuelto)
    fun observarProductos(): Flow<List<Producto>>
    suspend fun guardarProducto(p: Producto)
    suspend fun eliminarProducto(p: Producto)
    suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean)

    // Usuarios
    suspend fun login(correo: String, clave: String): Usuario?

    // Pedidos
    suspend fun crearPedido(
        clienteId: Int, items: List<ItemCarrito>,
        horaRecojo: String, pago: String, nota: String
    ): Long
    fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>>
    fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>>
    fun observarPedido(id: Int): Flow<PedidoConDetalle?>
    suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido)

    // Panel
    fun observarVentasHoy(inicioDia: Long): Flow<Double>
    fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int>

    fun observarPedidosPorHora(inicioDia: Long): Flow<List<PedidosPorHora>>
}