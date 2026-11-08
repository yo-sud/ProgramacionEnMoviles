package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.TIPO_POKEMON
import com.tecsup.pokemon.data.local.ItemDao
import com.tecsup.pokemon.data.mapper.aEntity
import com.tecsup.pokemon.data.mapper.aModelo
import com.tecsup.pokemon.data.remote.ApiService
import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    override val items = dao.todos().map { l -> l.map { it.aModelo() } }
    override val favoritos = dao.favoritos().map { l -> l.map { it.aModelo() } }

    override suspend fun refrescar(): Result<Int> = try {
        val favoritos = dao.idsFavoritos().toSet() // se conservan al sincronizar
        val nuevos = api.listarPorTipo(TIPO_POKEMON).pokemon
            .map { it.pokemon.aEntity(TIPO_POKEMON, esFavorito = it.pokemon.name in favoritos) }
        dao.guardarTodos(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: SerializationException) {
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: String): Result<Item> = try {
        val esFav = id in dao.idsFavoritos()
        Result.success(api.detalle(id).aModelo(esFav))
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: SerializationException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: String) = dao.alternarFavorito(id)
    override suspend fun cantidadGuardada() = dao.contar()
    override suspend fun cantidadFavoritos() = dao.contarFavoritos()
    override suspend fun limpiarCache() = dao.borrarTodo()
}
