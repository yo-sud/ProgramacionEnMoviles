package com.ochoa.myapplication
import java.util.Scanner
import java.util.Calendar
import java.text.SimpleDateFormat

fun main() {
    val scanner = Scanner(System.`in`)

    print("Ingrese su nombre de usuario: ")
    var usuario = scanner.nextLine()
    while (usuario.isBlank()) {
        print("Ingrese un nombre de usuario válido: ")
        usuario = scanner.nextLine()
    }
    println("\nBienvenido $usuario!")

    var continuar: String

    do {
        println("\n========================================")
        print("Ingrese el nombre del producto: ")
        var nombre = scanner.nextLine()
        while (nombre.isBlank()) {
            print("Ingrese un nombre válido: ")
            nombre = scanner.nextLine()
        }

        print("Ingrese el precio: ")
        var precioInput = scanner.nextLine()
        var precio = precioInput.toDoubleOrNull()
        while (precio == null || precio <= 0) {
            print("Ingresar un precio mayor a 0: ")
            precioInput = scanner.nextLine()
            precio = precioInput.toDoubleOrNull()
        }

        print("Ingrese la cantidad: ")
        var cantidadInput = scanner.nextLine()
        var cantidad = cantidadInput.toIntOrNull()
        while (cantidad == null || cantidad <= 0) {
            print("Ingresar un valor entero mayor a 0: ")
            cantidadInput = scanner.nextLine()
            cantidad = cantidadInput.toIntOrNull()
        }

        print("Ingrese las cuotas (6, 12 o 24): ")
        var cuotasInput = scanner.nextLine()
        var cuotas = cuotasInput.toIntOrNull()
        while (cuotas == null || (cuotas != 6 && cuotas != 12 && cuotas != 24)) {
            print("Solo se permite 6, 12 o 24 cuotas: ")
            cuotasInput = scanner.nextLine()
            cuotas = cuotasInput.toIntOrNull()
        }

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
        println(
            String.format(
                "Interés (%d%%): S/ %.2f",
                (porcentajeInteres * 100).toInt(),
                interes
            )
        )
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

            println(
                String.format(
                    "Cuota %d (%s): S/ %.2f | Saldo restante: S/ %.2f",
                    i, fechaPago, pagoMensual, saldoRestante
                )
            )
            i++
        } while (i <= cuotas)

        print("\n¿Desea realizar el cálculo de otro producto, $usuario? (S/N): ")
        continuar = scanner.nextLine().trim()

    } while (continuar.equals("S", ignoreCase = true))

    println("!Gracias por su compra, $usuario!")
}