package com.ochoa.myapplication
import java.util.Scanner
import java.util.Calendar
import java.text.SimpleDateFormat

fun main() {
    val scanner = Scanner(System.`in`)

    print("Ingrese el nombre del producto: ")
    var nombre = scanner.nextLine()
    while (nombre.isBlank()) {
        print("Ingrese un nombre valido, no lo deje en blanco: ")
        nombre = scanner.nextLine()
    }

    print("Ingrese el precio: ")
    var precio = scanner.nextDouble()
    while (precio <= 0) {
        print("El precio debe ser mayor a 0: ")
        precio = scanner.nextDouble()
    }

    print("Ingrese la cantidad: ")
    var cantidad = scanner.nextInt()
    while (cantidad <= 0) {
        print("La cantidad debe ser mayor a 0: ")
        cantidad = scanner.nextInt()
    }

    print("Ingrese las cuotas (6, 12 o 24): ")
    var cuotas = scanner.nextInt()
    while (cuotas != 6 && cuotas != 12 && cuotas != 24) {
        print("Solo se permite 6, 12 o 24: ")
        cuotas = scanner.nextInt()
    }

    println("\n--- DATOS INSERTADOS ---")
    println(String.format("Producto: %s | Precio: S/ %.2f | Cantidad: %d | Cuotas: %d", nombre, precio, cantidad, cuotas))

    val montoInicial = precio * cantidad

    val porcentajeInteres = when (cuotas) {
        6 -> 0.20
        12 -> 0.40
        else -> 0.60
    }

    val interes = montoInicial * porcentajeInteres
    val montoTotal = montoInicial + interes
    val pagoMensual = montoTotal / cuotas

    println("\n--- RESUMEN DE COMPRA ---")
    println(String.format("Producto: %s", nombre))
    println(String.format("Monto Inicial: S/ %.2f", montoInicial))
    println(String.format("Interés (%d%%): S/ %.2f", (porcentajeInteres * 100).toInt(), interes))
    println(String.format("Monto a Pagar: S/ %.2f", montoTotal))
    println(String.format("Pago Mensual: S/ %.2f", pagoMensual))

    println("\n--- CALENDARIO DE PAGOS ---")
    var saldoRestante = montoTotal
    val calendar = Calendar.getInstance()
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy")

    var i = 1
    do {
        calendar.add(Calendar.MONTH, 1)
        val fechaPago = formatoFecha.format(calendar.time)
        saldoRestante -= pagoMensual
        if (saldoRestante < 0.01) saldoRestante = 0.0

        println(String.format("Cuota %d (%s): S/ %.2f | Saldo restante: S/ %.2f",
            i, fechaPago, pagoMensual, saldoRestante))
        i++
    } while (i <= cuotas)
}