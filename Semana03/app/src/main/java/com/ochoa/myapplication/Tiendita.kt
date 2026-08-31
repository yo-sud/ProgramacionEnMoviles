package com.ochoa.myapplication
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)

    print("Ingrese el nombre del producto: ")
    var nombre = scanner.nextLine()
    while (nombre.isBlank()) {
        print("Error. Ingrese un nombre válido: ")
        nombre = scanner.nextLine()
    }

    print("Ingrese el precio: ")
    var precio = scanner.nextDouble()
    while (precio <= 0) {
        print("Error. Ingrese un precio mayor a 0: ")
        precio = scanner.nextDouble()
    }

    print("Ingrese la cantidad: ")
    var cantidad = scanner.nextInt()
    while (cantidad <= 0) {
        print("Error. Ingrese una cantidad mayor a 0: ")
        cantidad = scanner.nextInt()
    }

    print("Ingrese las cuotas (6, 12 o 24): ")
    var cuotas = scanner.nextInt()
    while (cuotas != 6 && cuotas != 12 && cuotas != 24) {
        print("Error. Solo se permite 6, 12 o 24: ")
        cuotas = scanner.nextInt()
    }

    println("\n--- DATOS INSERTADOS ---")
    println(String.format("Producto: %s | Precio: S/ %.2f | Cantidad: %d | Cuotas: %d", nombre, precio, cantidad, cuotas))

    val montoInicial = precio * cantidad
    println(String.format("Monto Inicial: S/ %.2f", montoInicial))
}