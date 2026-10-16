package com.tecsup.cafecampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tecsup.cafecampus.data.local.dao.PedidoDao
import com.tecsup.cafecampus.data.local.dao.ProductoDao
import com.tecsup.cafecampus.data.local.dao.UsuarioDao
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario

@Database(
    entities = [
        Producto::class,
        Usuario::class,
        Pedido::class,
        DetallePedido::class,
    ],
    version = 2,
    exportSchema = false
)
abstract class CafeDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pedidoDao(): PedidoDao

    companion object {
        @Volatile private var INSTANCE: CafeDatabase? = null

        /** Única instancia de la base para toda la app (singleton). */
        fun obtener(context: Context): CafeDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                CafeDatabase::class.java,
                "cafecampus.db"
            )
                // Solo para desarrollo: si cambia el esquema se recrea la base.
                // Antes de publicar una app real se reemplaza por Migrations.
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }
    }
}