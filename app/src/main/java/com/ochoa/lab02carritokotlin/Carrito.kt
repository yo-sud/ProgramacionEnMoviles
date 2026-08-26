package com.ochoa.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int,
)

fun mostrarDetalle(productos: List<Producto>) {
    println("-----DETALLE DEL CARRITO-----")
    var i = 1
    for (p in productos){
        val importe = p.precio*p.cantidad
        println(String.format("%d. %-20s x%d S/ %8.2f",
            i, p.nombre, p.cantidad, importe))
        i++
    }
    println("------------------------------")
}
fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio*p.cantidad

    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    val igv = subtotal * 0.18
    println(String.format("%-9s: S/ %.2f","IGV (18%)", igv))
    return igv
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    val total = subtotal + igv
    println(String.format("%-9s: S/ %.2f", "TOTAL",total))
    return total
}

fun main() {
    println("====================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("====================================")

    val nombreCliente = "Yamil Ochoa"
    val carrito = mutableListOf<Producto>()
    println("Cliente: $nombreCliente")

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Monitor Xiaomi 45",500.0,4 ))
    carrito.add(Producto("Arduino UNO ", 50.0,4))
    println()

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }
    println()
    mostrarDetalle(carrito)
    println()
    println("Cantidad de productos: ${carrito.size}")
    println()

    val subtotal = calcularSubtotal(carrito)
    println(String.format("%-9s: S/ %.2f", "Subtotal", subtotal))

    val igv = calcularIGV(subtotal)
    calcularTotal(subtotal, igv)
}