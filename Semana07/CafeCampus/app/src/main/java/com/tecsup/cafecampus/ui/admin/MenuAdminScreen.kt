package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.CafeChip
import com.tecsup.cafecampus.ui.components.EtiquetaChip
import com.tecsup.cafecampus.ui.components.ProductoIcono
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.ui.theme.EstadoCancelado
import com.tecsup.cafecampus.ui.theme.EstadoListo
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel

@Composable
fun MenuAdminScreen(
    vm: MenuAdminViewModel,
    onNuevo: () -> Unit,
    onEditar: (Int) -> Unit,
    onVolver: (() -> Unit)? = null
) {
    val todos by vm.productos.collectAsStateWithLifecycle()
    val productos by vm.productosVisibles.collectAsStateWithLifecycle()
    val categorias by vm.categorias.collectAsStateWithLifecycle()
    val categoria by vm.categoria.collectAsStateWithLifecycle()
    // Se guarda solo el id (sobrevive a rotaciones); el producto se busca en la lista actual
    var eliminarId by rememberSaveable { mutableStateOf<Int?>(null) }
    val aEliminar = todos.firstOrNull { it.id == eliminarId }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            AppTopBar("Gestión del menú", onVolver = onVolver, centrado = true)

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { CafeChip("Todo", categoria == null, onClick = { vm.elegirCategoria(null) }) }
                items(categorias) { c -> CafeChip(c, categoria == c, onClick = { vm.elegirCategoria(c) }) }
            }

            if (productos.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (todos.isEmpty()) "Aún no hay productos. Toca + para agregar." else "No hay productos en esta categoría",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    // El espacio inferior evita que el botón + tape la última tarjeta
                    contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 104.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productos, key = { it.id }) { p ->
                        TarjetaProducto(
                            p = p,
                            onAlternar = { vm.alternarDisponibilidad(p) },
                            onEditar = { onEditar(p.id) },
                            onEliminar = { eliminarId = p.id }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onNuevo,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp).size(64.dp),
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar producto", modifier = Modifier.size(30.dp))
        }
    }

    if (aEliminar != null) {
        AlertDialog(
            onDismissRequest = { eliminarId = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Eliminar \"${aEliminar.nombre}\" del menú? Los pedidos anteriores no se modifican.") },
            confirmButton = {
                TextButton(onClick = { vm.eliminar(aEliminar); eliminarId = null }) {
                    Text("Eliminar", color = EstadoCancelado)
                }
            },
            dismissButton = { TextButton(onClick = { eliminarId = null }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun TarjetaProducto(
    p: Producto,
    onAlternar: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val c = MaterialTheme.colorScheme
    CafeCard(relleno = 12) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProductoIcono(p.icono, tamano = 68.dp)

            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    p.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${p.categoria} · ${p.precio.soles()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = c.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // Tocar el chip alterna Disponible / Agotado
                EtiquetaChip(
                    texto = if (p.disponible) "Disponible" else "Agotado",
                    color = if (p.disponible) EstadoListo else EstadoCancelado,
                    modifier = Modifier.clickable(onClick = onAlternar)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                IconButton(onClick = onEditar, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar ${p.nombre}", tint = c.primary, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onEliminar, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar ${p.nombre}", tint = EstadoCancelado, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}