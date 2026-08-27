package com.ochoa.lab02carritokotlin

import java.util.Scanner

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int,
)

fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre.equals(nombre, ignoreCase = true) }
}

fun eliminarProducto(productos: MutableList<Producto>, nombre: String): Boolean {
    return productos.removeIf { it.nombre.equals(nombre, ignoreCase = true) }
}

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

    val scanner = Scanner(System.`in`)
    print("Cliente: ")
    val nombreCliente = scanner.nextLine()

    val carrito = mutableListOf<Producto>()
    println("Cliente: $nombreCliente")

    println("\nIngrese los productos (escriba 'fin' en nombre para terminar):")
    while (true) {
        print("Nombre: ")
        val nombre = scanner.nextLine()
        if (nombre.trim().lowercase() == "fin") break

        print("Precio: ")
        val precio = scanner.nextDouble()

        print("Cantidad: ")
        val cantidad = scanner.nextInt()
        scanner.nextLine()

        carrito.add(Producto(nombre, precio, cantidad))
    }
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

    println("\n--- OPCIONES ---")
    print("1. Buscar producto (escribe nombre o 'fin' para salir): ")
    val buscarNombre = scanner.nextLine()
    if (buscarNombre.trim().lowercase() != "fin" && buscarNombre.isNotEmpty()) {
        val encontrado = buscarProducto(carrito, buscarNombre)
        if (encontrado != null) {
            println("Producto encontrado: ${encontrado.nombre} (Precio: S/${encontrado.precio}, Cantidad: ${encontrado.cantidad})")
        } else {
            println("Producto no encontrado en el carrito.")
        }
    }

    print("\n2. Eliminar producto (escribe nombre o 'fin' para salir): ")
    val eliminarNombre = scanner.nextLine()
    if (eliminarNombre.trim().lowercase() != "fin" && eliminarNombre.isNotEmpty()) {
        val eliminado = eliminarProducto(carrito, eliminarNombre)
        if (eliminado) {
            println("Producto eliminado. Nuevo estado del carrito:")
            mostrarDetalle(carrito)

            println("Cantidad de productos: ${carrito.size}")
            val nuevoSubtotal = calcularSubtotal(carrito)
            println(String.format("%-9s: S/ %.2f", "Subtotal", nuevoSubtotal))
            val nuevoIGV = calcularIGV(nuevoSubtotal)
            val nuevoTotal = calcularTotal(nuevoSubtotal, nuevoIGV)
            println("------------------------------")
        } else {
            println("No se encontró el producto para eliminar.")
        }
    }

    println("¡Gracias por su compra, $nombreCliente!")

    scanner.close()
}