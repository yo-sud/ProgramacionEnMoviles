package com.tecsup.pokemon.data.remote

import com.tecsup.pokemon.data.remote.dto.DetalleDto
import com.tecsup.pokemon.data.remote.dto.TipoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("type/{tipo}")
    suspend fun listarPorTipo(@Path("tipo") tipo: String): TipoResponse

    @GET("pokemon/{nombre}")
    suspend fun detalle(@Path("nombre") nombre: String): DetalleDto
}
