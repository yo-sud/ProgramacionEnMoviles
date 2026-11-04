package com.tecsup.cafecampus.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tecsup.cafecampus.data.local.entity.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos")
    fun observarTodos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtener(id: Int): Producto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: Producto)

    @Delete
    suspend fun eliminar(producto: Producto)

    @Query("UPDATE productos SET disponible = :disponible WHERE id = :id")
    suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean)
}
