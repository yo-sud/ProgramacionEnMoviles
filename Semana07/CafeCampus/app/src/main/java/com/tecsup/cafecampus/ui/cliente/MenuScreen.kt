package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.ui.components.CafeChip
import com.tecsup.cafecampus.ui.components.ProductoCard
import com.tecsup.cafecampus.viewmodel.MenuViewModel

/**
 * La View solo observa el ViewModel y le avisa de los clics y del texto escrito:
 * el filtrado (categoría + búsqueda) y el carrito los maneja MenuViewModel.
 * `nombre` es el primer nombre del usuario que inició sesión.
 */
@Composable
fun MenuScreen(
    vm: MenuViewModel,
    nombre: String,
    onProducto: (Int) -> Unit,
    onBolsa: () -> Unit = {}
) {
    val productos by vm.productosVisibles.collectAsStateWithLifecycle()
    val categorias by vm.categorias.collectAsStateWithLifecycle()
    val categoria by vm.categoria.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val cantidad by vm.cantidadItems.collectAsStateWithLifecycle()

    val titulo = when {
        busqueda.isNotBlank() -> "Resultados"
        categoria != null -> categoria.orEmpty()
        else -> "Populares hoy"
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Encabezado(nombre = nombre, cantidad = cantidad, onBolsa = onBolsa)
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Buscador(texto = busqueda, onTexto = { vm.buscar(it) })
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { CafeChip("Todo", categoria == null, onClick = { vm.elegirCategoria(null) }) }
                items(categorias) { c -> CafeChip(c, categoria == c, onClick = { vm.elegirCategoria(c) }) }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (productos.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(Modifier.fillMaxWidth().padding(vertical = 48.dp), contentAlignment = Alignment.Center) {
                    Text(
                        if (busqueda.isBlank()) "No hay productos" else "Sin resultados para \"${busqueda.trim()}\"",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(productos, key = { it.id }) { p ->
                ProductoCard(
                    p = p,
                    onClick = { onProducto(p.id) },
                    onAgregar = { vm.agregar(p, 1) } // el "+" agrega 1 unidad directo a la bolsa
                )
            }
        }
    }
}

@Composable
private fun Encabezado(nombre: String, cantidad: Int, onBolsa: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                if (nombre.isBlank()) "¡Hola!" else "¡Hola, $nombre!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text("¿Qué se te antoja hoy?", color = colores.onSurfaceVariant)
        }
        IconButton(onClick = onBolsa) {
            BadgedBox(badge = {
                if (cantidad > 0) Badge(containerColor = colores.primary, contentColor = colores.onPrimary) { Text("$cantidad") }
            }) {
                Icon(Icons.Outlined.ShoppingBag, contentDescription = "Bolsa", modifier = Modifier.size(28.dp))
            }
        }
    }
}

@Composable
private fun Buscador(texto: String, onTexto: (String) -> Unit) {
    val foco = LocalFocusManager.current
    val c = MaterialTheme.colorScheme
    OutlinedTextField(
        value = texto,
        onValueChange = onTexto,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Buscar en el menú...") },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
        trailingIcon = {
            if (texto.isNotEmpty()) {
                IconButton(onClick = { onTexto("") }) { Icon(Icons.Filled.Close, contentDescription = "Borrar búsqueda") }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { foco.clearFocus() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = c.surfaceContainerLowest,
            unfocusedContainerColor = c.surfaceContainerLowest,
            unfocusedBorderColor = c.outlineVariant,
            focusedBorderColor = c.primary
        )
    )
}