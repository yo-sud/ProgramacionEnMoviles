import java.util.Scanner

abstract class ItemBase {
    abstract val nombre: String
    abstract val precioUnitario: Double
    abstract val cantidad: Int
    fun getSubtotal(): Double {
        return precioUnitario * cantidad
    }
    override fun toString(): String {
        return String.format("%-20s | %8s | %8s | %10s", nombre, precioUnitario, cantidad, getSubtotal())
    }
}
class Producto(
    override val nombre: String,
    override val precioUnitario: Double,
    override val cantidad: Int
) : ItemBase()
fun calcularSubtotal(carrito: List<ItemBase>): Double {
    return carrito.sumOf { it.getSubtotal() }
}
fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}
fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}
fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}
fun mostrarDetalle(carrito: MutableList<ItemBase>) {
    println("\n" + "=".repeat(60))
    println("Detalle de Productos".centerPad(60))
    println("=".repeat(60))
    println(String.format("%-20s | %8s | %8s | %10s", "Producto", "Precio", "Cant.", "Subtotal"))
    println("-".repeat(60))
    for (item in carrito) {
        println(item)
    }
    println("=".repeat(60))
}
fun String.centerPad(width: Int): String {
    val totalPad = width - this.length
    val padLeft = totalPad / 2
    val padRight = totalPad - padLeft
    return " ".repeat(padLeft) + this + " ".repeat(padRight)
}
fun main() {
    val scanner = Scanner(System.`in`)
    val carrito = mutableListOf<ItemBase>()
    for (i in 1..4) {
        println("\n--- Producto $i ---")
        print("Ingrese nombre del producto: ")
        val nombre = scanner.nextLine()
        print("Ingrese precio unitario: ")
        val precio = scanner.nextDouble()
        print("Ingrese cantidad: ")
        val cantidad = scanner.nextInt()
        scanner.nextLine()
        carrito.add(Producto(nombre, precio, cantidad))
    }
    mostrarDetalle(carrito)
    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)
    val descuento = calcularDescuento(total)
    val totalConDescuento = total - descuento
    val productoMasCarro = carrito.maxByOrNull { it.getSubtotal() }
    println("\n" + "=".repeat(60))
    println("Resumen del Carrito".centerPad(60))
    println("=".repeat(60))
    println(String.format("%-21s%.2f", "SUBTOTAL: ", subtotal))
    println(String.format("%-21s%.2f", "IGV (18%): ", igv))
    println(String.format("%-21s%.2f", "TOTAL A PAGAR: ", total))
    if (descuento > 0) {
        val porcentaje = when {
            total > 5000 -> 10
            total > 3000 -> 5
            else -> 0
        }
        println(String.format("%-21sDESCUENTO APLICADO (%d%%): %.2f", "", porcentaje, descuento))
        println(String.format("%-21s%.2f", "TOTAL CON DESCUENTO: ", totalConDescuento))
    } else {
        println(String.format("%-21sNINGUNO", "", ""))
        println(String.format("%-21s%.2f", "TOTAL CON DESCUENTO: ", total))
    }
    println("-".repeat(60))
    println("Cantidad de productos: ${carrito.size}")
    productoMasCarro?.let {
        println("Producto más caro: ${it.nombre} - S/ ${it.getSubtotal()}")
    }
    println("=".repeat(60))
    scanner.close()
}
