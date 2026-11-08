package com.tecsup.pokemon.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Item

@Composable
fun ItemFila(item: Item, onClick: () -> Unit, onFavorito: () -> Unit) = Row(
    Modifier.fillMaxWidth().clickable(onClick = onClick).padding(12.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    AsyncImage(item.imagenUrl, item.titulo, Modifier.size(56.dp))
    Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
        Text(item.titulo)
        Text(item.subtitulo)
    }
    TextButton(onClick = onFavorito) { Text(if (item.esFavorito) "★" else "☆") }
}
