package com.tecsup.cafecampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.entity.Usuario

/** Un pedido con sus líneas y su cliente. */
data class PedidoConDetalle(
    @Embedded val pedido: Pedido,
    @Relation(parentColumn = "id", entityColumn = "pedidoId")
    val detalles: List<DetallePedido>,
    @Relation(parentColumn = "clienteId", entityColumn = "id")
    val cliente: Usuario
)