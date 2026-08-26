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

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000-> total * 0.10
        total > 3000-> total * 0.05
        else-> 0.0
    }
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
    carrito.add(Producto("Monitor Xiaomi 45",1500.0,4 ))
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
    val total = calcularTotal(subtotal, igv)

    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " +
                String.format("(S/ %.2f)", masCaro.precio))
    }

    val descuento = calcularDescuento(total)
    val porcentajeTexto = when {
        total > 5000 -> "10% por compra mayor a S/ 5000"
        total > 3000 -> "5% por compra mayor a S/ 3000"
        else -> "0%"
    }
    if (descuento > 0) {
        println("Descuento aplicado: $porcentajeTexto")
    }

    val totalConDescuento = total - descuento
    println(String.format("%-26s: S/ %.2f", "TOTAL CON DESCUENTO", totalConDescuento))

    println("¡Gracias por su compra, $nombreCliente!")
}