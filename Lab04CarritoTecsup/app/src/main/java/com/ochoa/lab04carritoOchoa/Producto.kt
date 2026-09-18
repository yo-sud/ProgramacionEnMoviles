package com.ochoa.lab04carritoOchoa

data class Producto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int
) {
    val importe: Double get() = precio * cantidad
}