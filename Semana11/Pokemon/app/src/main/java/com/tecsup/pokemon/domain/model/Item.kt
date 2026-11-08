package com.tecsup.pokemon.domain.model

data class Item(
    val id: String,            // nombre del pokémon (PokeAPI usa texto)
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String?,
    val esFavorito: Boolean = false,
    val detalle: String = ""   // solo se llena en el detalle
)
