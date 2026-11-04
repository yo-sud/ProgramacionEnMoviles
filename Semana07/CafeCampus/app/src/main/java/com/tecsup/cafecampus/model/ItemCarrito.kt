package com.tecsup.cafecampus.model

import com.tecsup.cafecampus.data.local.entity.Producto

data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int = 1,
    val nota: String = ""
) {
    val subtotal: Double get() = producto.precio * cantidad
}
