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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.EtiquetaChip
import com.tecsup.cafecampus.ui.components.ProductoIcono
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.ui.theme.EstadoCancelado
import com.tecsup.cafecampus.ui.theme.EstadoListo
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel

@Composable
fun MenuAdminScreen(vm: MenuAdminViewModel, onNuevo: () -> Unit, onEditar: (Int) -> Unit) {
    val productos by vm.productos.collectAsStateWithLifecycle()
    // Se guarda solo el id (sobrevive a rotaciones); el producto se busca en la lista actual
    var eliminarId by rememberSaveable { mutableStateOf<Int?>(null) }
    val aEliminar = productos.firstOrNull { it.id == eliminarId }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            AppTopBar("Menú")

            if (productos.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aún no hay productos. Toca + para agregar.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productos, key = { it.id }) { p ->
                        CafeCard {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ProductoIcono(p.icono, tamano = 48.dp)
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(p.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text("${p.categoria} · ${p.precio.soles()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    // Tocar el chip alterna Disponible / Agotado
                                    EtiquetaChip(
                                        texto = if (p.disponible) "Disponible" else "Agotado",
                                        color = if (p.disponible) EstadoListo else EstadoCancelado,
                                        modifier = Modifier.clickable { vm.alternarDisponibilidad(p) }
                                    )
                                }
                                IconButton(onClick = { onEditar(p.id) }) { Icon(Icons.Filled.Edit, contentDescription = "Editar") }
                                IconButton(onClick = { eliminarId = p.id }) { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onNuevo,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) { Icon(Icons.Filled.Add, contentDescription = "Agregar producto") }
    }

    if (aEliminar != null) {
        AlertDialog(
            onDismissRequest = { eliminarId = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Eliminar \"${aEliminar.nombre}\" del menú? Los pedidos anteriores no se modifican.") },
            confirmButton = {
                TextButton(onClick = { vm.eliminar(aEliminar); eliminarId = null }) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { eliminarId = null }) { Text("Cancelar") } }
        )
    }
}