package com.tecsup.pokemon.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Item

@Composable
fun ItemFila(item: Item, onClick: () -> Unit, onFavorito: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(item.imagenUrl, item.titulo, Modifier.size(64.dp))
            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(item.titulo, style = MaterialTheme.typography.titleMedium)
                Text(
                    item.subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onFavorito) {
                Icon(
                    imageVector = if (item.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Marcar favorito",
                    tint = if (item.esFavorito) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}