package com.ochoa.tecsupstore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaInicio(
    favoritos: List<Int>,
    onToggleFavorito: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    val categorias = listOf("Todos") + productosEjemplo.map { it.categoria }.distinct()

    val productosFiltrados = if (categoriaSeleccionada == "Todos") {
        productosEjemplo
    } else {
        productosEjemplo.filter { it.categoria == categoriaSeleccionada }
    }
    val secciones = productosFiltrados.groupBy { it.categoria }

    Column(modifier = modifier) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { categoria ->
                FilterChip(
                    selected = categoria == categoriaSeleccionada,
                    onClick = { categoriaSeleccionada = categoria },
                    label = { Text(categoria) }
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            secciones.forEach { (categoria, lista) ->
                item(key = "header_$categoria") {
                    Text(
                        text = categoria,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                items(lista, key = { it.id }) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        esFavorito = producto.id in favoritos,
                        onToggleFavorito = { onToggleFavorito(producto.id) }
                    )
                }
            }
        }
    }
}