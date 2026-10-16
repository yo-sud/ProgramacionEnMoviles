package com.tecsup.cafecampus.ui

import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle

/** "2 × Café americano, 1 × Empanada" */
fun PedidoConDetalle.resumenLineas(): String =
    detalles.joinToString(", ") { "${it.cantidad} × ${it.nombreProducto}" }