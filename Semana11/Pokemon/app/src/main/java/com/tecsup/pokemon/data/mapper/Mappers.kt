package com.tecsup.pokemon.data.mapper

import com.tecsup.pokemon.data.local.ItemEntity
import com.tecsup.pokemon.data.remote.dto.DetalleDto
import com.tecsup.pokemon.data.remote.dto.ItemDto
import com.tecsup.pokemon.domain.model.Item

private fun String.capitalizar() = replaceFirstChar { it.uppercase() }

private fun spriteUrl(url: String): String {
    val numero = url.trimEnd('/').substringAfterLast('/')
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$numero.png"
}

fun ItemDto.aEntity(tipo: String, esFavorito: Boolean) = ItemEntity(
    id = name,
    titulo = name.capitalizar(),
    subtitulo = "Tipo: $tipo",
    imagenUrl = spriteUrl(url),
    esFavorito = esFavorito
)

fun ItemEntity.aModelo() = Item(id, titulo, subtitulo, imagenUrl, esFavorito)

fun DetalleDto.aModelo(esFavorito: Boolean) = Item(
    id = name,
    titulo = name.capitalizar(),
    subtitulo = types.joinToString(" / ") { it.type.name },
    imagenUrl = sprites.frontDefault,
    esFavorito = esFavorito,
    detalle = "Altura: ${height / 10.0} m\nPeso: ${weight / 10.0} kg\n" +
            "Habilidades: ${abilities.joinToString { it.ability.name }}\n\n" +
            "Estadísticas base:\n" +
            stats.joinToString("\n") { "${it.stat.name}: ${it.baseStat}" }
)