package com.ochoa.lab02carritokotlin

import kotlin.math.roundToInt

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

data class ResumenCuenta(
    val subtotal: Double,
    val igv: Double,
    val total: Double,
    val descuento: Double,
    val totalFinal: Double
)

fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre.equals(nombre, ignoreCase = true) }
}

fun eliminarProducto(productos: MutableList<Producto>, nombre: String): Boolean {
    return productos.removeIf { it.nombre.equals(nombre, ignoreCase = true) }
}

fun mostrarDetalle(productos: List<Producto>) {
    println("\n----- DETALLE DEL CARRITO -----")
    if (productos.isEmpty()) {
        println("El carrito está vacío.")
    } else {
        var i = 1
        for (p in productos) {
            val importe = p.precio * p.cantidad
            println("${i++}. ${p.nombre.padEnd(20)} x${p.cantidad}  S/ ${String.format("%.2f", importe)}")
        }
    }
    println("--------------------------------")
}

fun mostrarResumen(resumen: ResumenCuenta) {
    println("\n----- RESUMEN DE COMPRA -----")
    println("Subtotal : S/ ${String.format("%.2f", resumen.subtotal)}")
    println("IGV (18%) : S/ ${String.format("%.2f", resumen.igv)}")
    println("Descuento : S/ ${String.format("%.2f", resumen.descuento)}")
    println("TOTAL     : S/ ${String.format("%.2f", resumen.totalFinal)}")
    println("----------------------------")
}

fun calcularResumen(productos: List<Producto>): ResumenCuenta {
    val subtotal = productos.sumOf { it.precio * it.cantidad }
    val igv = subtotal * 0.18
    val totalAntesDescuento = subtotal + igv

    val descuento = when {
        totalAntesDescuento > 5000 -> totalAntesDescuento * 0.10
        totalAntesDescuento > 3000 -> totalAntesDescuento * 0.05
        else -> 0.0
    }

    val totalFinal = totalAntesDescuento - descuento

    return ResumenCuenta(subtotal, igv, totalAntesDescuento, descuento, totalFinal)
}

fun leerNumeroEntero(mensaje: String): Int? {
    print(mensaje)
    return try {
        readLine()?.trim()?.toIntOrNull()
    } catch (e: Exception) {
        null
    }
}

fun leerNumeroDouble(mensaje: String): Double? {
    print(mensaje)
    return try {
        readLine()?.trim()?.toDoubleOrNull()
    } catch (e: Exception) {
        null
    }
}

fun main() {
    println("====================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("====================================")

    print("Cliente: ")
    val nombreCliente = readLine()?.trim() ?: "Desconocido"
    println("Bienvenido, $nombreCliente\n")

    val carrito = mutableListOf<Producto>()

    println("Ingrese los productos (Escriba 'FIN' en nombre para terminar):")
    while (true) {
        print("Nombre del producto: ")
        val input = readLine()?.trim()

        if (input.isNullOrBlank() || input.lowercase() == "fin") break

        val precio = leerNumeroDouble("Precio unitario (S/): ")
        if (precio == null || precio < 0) {
            println(">> Error: Ingrese un precio válido y positivo.\n")
            continue
        }

        val cantidad = leerNumeroEntero("Cantidad: ")
        if (cantidad == null || cantidad <= 0) {
            println(">> Error: Ingrese una cantidad válida mayor a 0.\n")
            continue
        }

        carrito.add(Producto(input, precio, cantidad))
        println(">> Producto agregado correctamente.")
    }

    if (carrito.isNotEmpty()) {
        mostrarDetalle(carrito)

        val productoMasCaro = carrito.maxByOrNull { it.precio }
        if (productoMasCaro != null) {
            println("\nProducto más caro: ${productoMasCaro.nombre} (S/ ${String.format("%.2f", productoMasCaro.precio)})")
        }

        println("\nOpciones disponibles:")
        println("1. Buscar un producto")
        println("2. Eliminar un producto")
        println("3. Ver resumen de compra")
        print("Seleccione una opción: ")

        val opcion = readLine()?.trim()

        when (opcion) {
            "1" -> {
                print("\n¿Qué producto desea buscar? (Escriba 'VOLVER' para regresar): ")
                val buscar = readLine()?.trim()
                if (!buscar.isNullOrBlank() && buscar.lowercase() != "volver") {
                    val encontrado = buscarProducto(carrito, buscar)
                    if (encontrado != null) {
                        println(">> Encontrado: ${encontrado.nombre} | Precio: S/${encontrado.precio} | Cantidad: ${encontrado.cantidad}")
                    } else {
                        println(">> No se encontró el producto en el carrito.")
                    }
                }
            }
            "2" -> {
                print("\n¿Qué producto desea eliminar? (Escriba 'VOLVER' para regresar): ")
                val eliminar = readLine()?.trim()
                if (!eliminar.isNullOrBlank() && eliminar.lowercase() != "volver") {
                    if (eliminarProducto(carrito, eliminar)) {
                        println(">> Producto eliminado. Actualizando detalle...")
                        mostrarDetalle(carrito)
                    } else {
                        println(">> No se encontró el producto para eliminar.")
                    }
                }
            }
            "3" -> {
                val resumen = calcularResumen(carrito)
                mostrarResumen(resumen)
            }
            else -> {
                println("Opción no válida.")
            }
        }
    } else {
        println("No se agregaron productos al carrito.")
    }

    println("\n¡Gracias por su compra, $nombreCliente!")
}