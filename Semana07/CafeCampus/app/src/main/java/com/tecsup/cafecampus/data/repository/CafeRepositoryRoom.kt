package com.tecsup.cafecampus.data.repository

import androidx.room.withTransaction
import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow
import com.tecsup.cafecampus.model.PedidosPorHora

/** Implementación local (Room) del contrato CafeRepository. */
class CafeRepositoryRoom(private val db: CafeDatabase) : CafeRepository {

    private val productos = db.productoDao()
    private val usuarios = db.usuarioDao()
    private val pedidos = db.pedidoDao()

    // ---- Productos (ejemplo resuelto) ----
    override fun observarProductos(): Flow<List<Producto>> = productos.observarTodos()
    override suspend fun guardarProducto(p: Producto) { productos.insertar(p) }
    override suspend fun eliminarProducto(p: Producto) = productos.eliminar(p)
    override suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean) =
        productos.cambiarDisponibilidad(id, disponible)

    // ---- Usuarios ----
    override suspend fun login(correo: String, clave: String): Usuario? = usuarios.login(correo, clave)

    // ---- Pedidos ----
    override suspend fun crearPedido(
        clienteId: Int, items: List<ItemCarrito>,
        horaRecojo: String, pago: String, nota: String
    ): Long {
        require(items.isNotEmpty()) { "El pedido no tiene productos" }
        return db.withTransaction {
            // Se vuelve a leer cada producto: así se usa el precio actual y se rechaza lo que ya no está disponible.
            val detalles = items.map { item ->
                val actual = productos.obtener(item.producto.id)
                check(actual != null && actual.disponible) { "\"${item.producto.nombre}\" ya no está disponible" }
                DetallePedido(
                    pedidoId = 0, // lo asigna PedidoDao.crearPedido
                    productoId = actual.id,
                    nombreProducto = actual.nombre,   // "foto" del momento
                    cantidad = item.cantidad,
                    precioUnitario = actual.precio    // "foto" del momento
                )
            }
            val total = detalles.sumOf { it.cantidad * it.precioUnitario }
            val pedido = Pedido(
                clienteId = clienteId, horaRecojo = horaRecojo,
                pago = pago, nota = nota, total = total
            )
            pedidos.crearPedido(pedido, detalles)
        }
    }

    override fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>> =
        pedidos.observarPorCliente(clienteId)

    override fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>> =
        pedidos.observarPorEstado(estado.name)

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> = pedidos.observarUno(id)

    override suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido) =
        pedidos.cambiarEstado(pedidoId, estado.name)

    // ---- Panel ----
    override fun observarVentasHoy(inicioDia: Long): Flow<Double> = pedidos.ventasDesde(inicioDia)

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> =
        pedidos.cantidadPorEstado(estado.name)
    override fun observarPedidosPorHora(inicioDia: Long): Flow<List<PedidosPorHora>> =
        pedidos.pedidosPorHora(inicioDia)
}