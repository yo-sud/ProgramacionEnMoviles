package com.tecsup.cafecampus.model

/** Cantidad de pedidos de una hora del día (0-23). Room llena esta clase directamente desde la consulta. */
data class PedidosPorHora(
    val hora: Int,
    val cantidad: Int
)