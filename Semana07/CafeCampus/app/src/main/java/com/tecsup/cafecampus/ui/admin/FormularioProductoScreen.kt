package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.ui.components.ICONOS_PRODUCTO
import com.tecsup.cafecampus.ui.components.iconoDe
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel
import java.util.Locale

/** Crea (productoId = 0) o edita (productoId existente) un producto del menú. */
@Composable
fun FormularioProductoScreen(vm: MenuAdminViewModel, productoId: Int, onListo: () -> Unit) {
    val productos by vm.productos.collectAsStateWithLifecycle()
    val existente = productos.firstOrNull { it.id == productoId }

    var nombre by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var disponible by rememberSaveable { mutableStateOf(true) }
    var icono by rememberSaveable { mutableStateOf("cafe") }
    var cargado by rememberSaveable { mutableStateOf(productoId == 0) }
    var intento by rememberSaveable { mutableStateOf(false) }

    // Al editar: se rellenan los campos una sola vez cuando llega el producto
    LaunchedEffect(existente) {
        if (!cargado && existente != null) {
            nombre = existente.nombre
            categoria = existente.categoria
            precio = String.format(Locale.US, "%.2f", existente.precio)
            descripcion = existente.descripcion
            disponible = existente.disponible
            icono = existente.icono
            cargado = true
        }
    }

    val precioNum = precio.replace(',', '.').toDoubleOrNull()
    val errorNombre = intento && nombre.isBlank()
    val errorPrecio = intento && (precioNum == null || precioNum <= 0.0)

    Column(Modifier.fillMaxSize()) {
        AppTopBar(if (productoId == 0) "Nuevo producto" else "Editar producto", onVolver = onListo)

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CafeTextField(nombre, { nombre = it }, "Nombre", error = errorNombre)
            if (errorNombre) Text("El nombre es obligatorio", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

            CafeTextField(categoria, { categoria = it }, "Categoría (ej. Bebidas)")

            CafeTextField(precio, { precio = it }, "Precio (S/)", teclado = KeyboardType.Decimal, error = errorPrecio)
            if (errorPrecio) Text("El precio debe ser mayor que 0", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

            CafeTextField(descripcion, { descripcion = it }, "Descripción", singleLine = false, minLineas = 2)

            Text("Ícono", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ICONOS_PRODUCTO.forEach { clave ->
                    FilterChip(
                        selected = icono == clave,
                        onClick = { icono = clave },
                        label = { Text(clave) },
                        leadingIcon = { Icon(iconoDe(clave), contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Disponible", style = MaterialTheme.typography.titleMedium)
                Switch(checked = disponible, onCheckedChange = { disponible = it })
            }

            CafeButton(
                texto = if (productoId == 0) "Agregar producto" else "Guardar cambios",
                onClick = {
                    intento = true
                    if (nombre.isNotBlank() && precioNum != null && precioNum > 0.0) {
                        vm.guardar(
                            Producto(
                                id = productoId,
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                categoria = categoria.trim().ifBlank { "General" },
                                precio = precioNum,
                                disponible = disponible,
                                icono = icono
                            )
                        )
                        onListo()
                    }
                }
            )
        }
    }
}