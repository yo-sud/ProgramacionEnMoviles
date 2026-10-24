package com.tecsup.cafecampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow
import com.tecsup.cafecampus.model.PedidosPorHora

@Dao
abstract class PedidoDao {

    // ---- Escritura (suspend) ----
    @Insert
    abstract suspend fun insertarPedido(p: Pedido): Long

    @Insert
    abstract suspend fun insertarDetalles(lista: List<DetallePedido>)

    @Query("UPDATE pedidos SET estado = :estado WHERE id = :id")
    abstract suspend fun cambiarEstado(id: Int, estado: String)

    /** Pedido + líneas en UNA transacción: o se guarda todo o nada. */
    @Transaction
    open suspend fun crearPedido(pedido: Pedido, detalles: List<DetallePedido>): Long {
        val id = insertarPedido(pedido)
        insertarDetalles(detalles.map { it.copy(pedidoId = id.toInt()) })
        return id
    }

    // ---- Lectura (Flow: la pantalla se actualiza sola) ----
    @Transaction
    @Query("SELECT * FROM pedidos WHERE clienteId = :clienteId ORDER BY fecha DESC")
    abstract fun observarPorCliente(clienteId: Int): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE estado = :estado ORDER BY fecha DESC")
    abstract fun observarPorEstado(estado: String): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :id")
    abstract fun observarUno(id: Int): Flow<PedidoConDetalle?>

    // ---- Panel ----
    @Query("SELECT COALESCE(SUM(total), 0.0) FROM pedidos WHERE fecha >= :inicio AND estado != 'CANCELADO'")
    abstract fun ventasDesde(inicio: Long): Flow<Double>

    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = :estado")
    abstract fun cantidadPorEstado(estado: String): Flow<Int>

    @Query(
        """
        SELECT CAST(strftime('%H', fecha / 1000, 'unixepoch', 'localtime') AS INTEGER) AS hora,
               COUNT(*) AS cantidad
        FROM pedidos
        WHERE fecha >= :inicio AND estado != 'CANCELADO'
        GROUP BY hora
        ORDER BY hora
        """
    )
    abstract fun pedidosPorHora(inicio: Long): Flow<List<PedidosPorHora>>
}