package com.tecsup.cafecampus.data.local

import androidx.room.withTransaction
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario

/** Carga los datos de prueba solo la primera vez (datos ficticios, Ley 29733). */
suspend fun sembrar(db: CafeDatabase) {
    db.withTransaction { sembrarSiVacia(db) }
}

private suspend fun sembrarSiVacia(db: CafeDatabase) {
    if (db.usuarioDao().contar() == 0) {
        productosIniciales().forEach { db.productoDao().insertar(it) }
        usuariosIniciales().forEach { db.usuarioDao().insertar(it) }
    }
}

private fun productosIniciales() = listOf(
    Producto(nombre = "Café americano", descripcion = "Café filtrado recién hecho", categoria = "Bebidas", precio = 4.00, icono = "cafe"),
    Producto(nombre = "Sándwich de pollo", descripcion = "Pan artesanal con pollo deshilachado", categoria = "Comidas", precio = 9.00, icono = "pan"),
    Producto(nombre = "Menú del día", descripcion = "Entrada, segundo y refresco", categoria = "Comidas", precio = 12.00, icono = "plato"),
    Producto(nombre = "Jugo de naranja", descripcion = "Natural, sin azúcar añadida", categoria = "Bebidas", precio = 5.00, icono = "jugo"),
    Producto(nombre = "Empanada", descripcion = "Horneada, de carne o pollo", categoria = "Snacks", precio = 4.50, icono = "dulce"),
    Producto(nombre = "Chicha morada", descripcion = "Receta tradicional peruana", categoria = "Bebidas", precio = 3.50, icono = "botella")
)

private fun usuariosIniciales() = listOf(
    Usuario(nombre = "Juan Pérez", correo = "juan@campus.test", clave = "1234", rol = "CLIENTE"),
    Usuario(nombre = "Admin Cafetería", correo = "admin@cafe.test", clave = "admin", rol = "ADMIN")
)