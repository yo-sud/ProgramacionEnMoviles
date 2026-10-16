package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.ui.components.EtiquetaChip
import com.tecsup.cafecampus.ui.components.ProductoIcono
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.ui.theme.EstadoCancelado
import com.tecsup.cafecampus.ui.theme.EstadoListo
import com.tecsup.cafecampus.viewmodel.MenuViewModel

@Composable
fun DetalleProductoScreen(vm: MenuViewModel, productoId: Int, onVolver: () -> Unit) {
    val productos by vm.productos.collectAsStateWithLifecycle()
    val producto = productos.firstOrNull { it.id == productoId }
    var cantidad by rememberSaveable { mutableIntStateOf(1) }
    var nota by rememberSaveable { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Detalle", onVolver = onVolver)

        if (producto == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Producto no encontrado") }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProductoIcono(producto.icono, Modifier.align(Alignment.CenterHorizontally), 120.dp)

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(producto.nombre, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                    if (producto.disponible) EtiquetaChip("Disponible", EstadoListo)
                    else EtiquetaChip("Agotado", EstadoCancelado)
                }
                Text(producto.precio.soles(), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text(producto.descripcion, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(onClick = { if (cantidad > 1) cantidad-- }, enabled = cantidad > 1) {
                        Icon(Icons.Filled.Remove, contentDescription = "Menos")
                    }
                    Text("$cantidad", style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = { if (cantidad < 20) cantidad++ }) {
                        Icon(Icons.Filled.Add, contentDescription = "Más")
                    }
                }

                CafeTextField(
                    valor = nota, onValor = { nota = it },
                    etiqueta = "Nota para la cafetería (opcional)",
                    singleLine = false, minLineas = 2
                )

                CafeButton(
                    texto = if (producto.disponible) "Agregar al pedido · ${(producto.precio * cantidad).soles()}" else "No disponible",
                    enabled = producto.disponible,
                    onClick = {
                        vm.agregar(producto, cantidad, nota.trim())
                        onVolver()
                    }
                )
            }
        }
    }
}