package com.tecsup.cafecampus.ui

import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle

/** "2 × Café americano, 1 × Empanada" */
fun PedidoConDetalle.resumenLineas(): String =
    detalles.joinToString(", ") { "${it.cantidad} × ${it.nombreProducto}" }

/** "Menú del día x2 + 1 más": primer producto y cuántos más lleva el pedido. */
fun PedidoConDetalle.resumenCorto(): String {
    val primero = detalles.firstOrNull() ?: return "Sin productos"
    val base = "${primero.nombreProducto} x${primero.cantidad}"
    val resto = detalles.size - 1
    return if (resto > 0) "$base + $resto más" else base
}

/** "1 x Sándwich pollo + 1 más": formato del panel del administrador. */
fun PedidoConDetalle.resumenAdmin(): String {
    val primero = detalles.firstOrNull() ?: return "Sin productos"
    val base = "${primero.cantidad} x ${primero.nombreProducto}"
    val resto = detalles.size - 1
    return if (resto > 0) "$base + $resto más" else base
}

/** "1 x Café + 1 x Keke": todos los productos del pedido, para la lista del administrador. */
fun PedidoConDetalle.resumenCompleto(): String {
    if (detalles.isEmpty()) return "Sin productos"
    return detalles.joinToString(" + ") { "${it.cantidad} x ${it.nombreProducto}" }
}