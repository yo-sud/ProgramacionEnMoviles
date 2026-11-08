package com.tecsup.pokemon.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// GET /type/{tipo}
@Serializable data class TipoResponse(val pokemon: List<PokemonSlotDto> = emptyList())
@Serializable data class PokemonSlotDto(val pokemon: ItemDto)
@Serializable data class ItemDto(val name: String, val url: String)

// GET /pokemon/{nombre}
@Serializable
data class DetalleDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: SpritesDto = SpritesDto(),
    val types: List<TipoSlotDto> = emptyList(),
    val abilities: List<HabilidadSlotDto> = emptyList()
)
@Serializable data class SpritesDto(@SerialName("front_default") val frontDefault: String? = null)
@Serializable data class TipoSlotDto(val type: NombreDto)
@Serializable data class HabilidadSlotDto(val ability: NombreDto)
@Serializable data class NombreDto(val name: String)
