package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.ui.components.ICONOS_PRODUCTO
import com.tecsup.cafecampus.ui.components.ProductoIcono
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel
import java.util.Locale

/** Crea (productoId = 0) o edita (productoId existente) un producto del menú. */
@Composable
fun FormularioProductoScreen(vm: MenuAdminViewModel, productoId: Int, onListo: () -> Unit) {
    val productos by vm.productos.collectAsStateWithLifecycle()
    val sugeridas by vm.categoriasDisponibles.collectAsStateWithLifecycle()
    val existente = productos.firstOrNull { it.id == productoId }
    val c = MaterialTheme.colorScheme

    var nombre by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var disponible by rememberSaveable { mutableStateOf(true) }
    var icono by rememberSaveable { mutableStateOf("cafe") }
    var cargado by rememberSaveable { mutableStateOf(productoId == 0) }
    var intento by rememberSaveable { mutableStateOf(false) }
    var verImagenes by rememberSaveable { mutableStateOf(false) }
    var verCategorias by rememberSaveable { mutableStateOf(false) }

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
    val errorCategoria = intento && categoria.isBlank()
    val errorPrecio = intento && (precioNum == null || precioNum <= 0.0)

    Column(Modifier.fillMaxSize()) {
        AppTopBar(
            if (productoId == 0) "Nuevo producto" else "Editar producto",
            onVolver = onListo,
            centrado = true
        )

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen del producto: al tocar se elige una de las ilustraciones
            CafeCard(onClick = { verImagenes = true }, colorFondo = c.primaryContainer) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProductoIcono(icono, tamano = 64.dp)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Imagen del producto", fontWeight = FontWeight.Bold, color = c.primary)
                        Text("Toca para elegir una imagen", style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant)
                    }
                }
            }

            CafeTextField(nombre, { nombre = it }, "Nombre del producto", error = errorNombre)
            if (errorNombre) MensajeError("El nombre es obligatorio")

            // Categoría: campo de solo lectura que abre la lista de categorías
            Box {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null) },
                    isError = errorCategoria,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = c.primary,
                        unfocusedBorderColor = c.outlineVariant
                    )
                )
                Box(Modifier.matchParentSize().clip(RoundedCornerShape(12.dp)).clickable { verCategorias = true })
            }
            if (errorCategoria) MensajeError("Elige una categoría")

            CafeTextField(precio, { precio = it }, "Precio (S/)", teclado = KeyboardType.Decimal, error = errorPrecio)
            if (errorPrecio) MensajeError("El precio debe ser mayor que 0")

            CafeTextField(descripcion, { descripcion = it }, "Descripción", singleLine = false, minLineas = 3)

            CafeCard {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Disponible en el menú", fontWeight = FontWeight.Bold)
                        Text(
                            if (disponible) "Los estudiantes podrán pedirlo" else "Los estudiantes no podrán pedirlo",
                            style = MaterialTheme.typography.bodySmall,
                            color = c.onSurfaceVariant
                        )
                    }
                    Switch(checked = disponible, onCheckedChange = { disponible = it })
                }
            }

            Spacer(Modifier.padding(top = 4.dp))

            CafeButton(
                texto = if (productoId == 0) "Guardar producto" else "Guardar cambios",
                onClick = {
                    intento = true
                    if (nombre.isNotBlank() && categoria.isNotBlank() && precioNum != null && precioNum > 0.0) {
                        vm.guardar(
                            Producto(
                                id = productoId,
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                categoria = categoria.trim(),
                                precio = precioNum,
                                disponible = disponible,
                                icono = icono
                            )
                        )
                        onListo()
                    }
                }
            )
            CafeButton(texto = "Cancelar", secundario = true, onClick = onListo)
        }
    }

    if (verImagenes) {
        DialogoImagen(actual = icono, onElegir = { icono = it; verImagenes = false }, onCerrar = { verImagenes = false })
    }
    if (verCategorias) {
        DialogoCategoria(
            categorias = (sugeridas + categoria).filter { it.isNotBlank() }.distinct(),
            actual = categoria,
            onElegir = { categoria = it; verCategorias = false },
            onCerrar = { verCategorias = false }
        )
    }
}

@Composable
private fun MensajeError(texto: String) {
    Text(texto, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
}

/** Cuadrícula con las 6 ilustraciones; cada una trae su propio color de fondo. */
@Composable
private fun DialogoImagen(actual: String, onElegir: (String) -> Unit, onCerrar: () -> Unit) {
    val c = MaterialTheme.colorScheme
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Elige una imagen") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ICONOS_PRODUCTO.chunked(3).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        fila.forEach { clave ->
                            val activa = clave == actual
                            val forma = RoundedCornerShape(14.dp)
                            Box(
                                Modifier
                                    .clip(forma)
                                    .border(if (activa) 3.dp else 1.dp, if (activa) c.primary else c.outlineVariant, forma)
                                    .clickable { onElegir(clave) }
                                    .padding(3.dp)
                            ) {
                                ProductoIcono(clave, tamano = 58.dp)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onCerrar) { Text("Listo") } }
    )
}

/** Lista de categorías con una opción para escribir una nueva. */
@Composable
private fun DialogoCategoria(
    categorias: List<String>,
    actual: String,
    onElegir: (String) -> Unit,
    onCerrar: () -> Unit
) {
    var otra by rememberSaveable { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Categoría") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                categorias.forEach { cat ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onElegir(cat) }
                            .padding(horizontal = 4.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = cat == actual, onClick = null)
                        Spacer(Modifier.width(12.dp))
                        Text(cat)
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                CafeTextField(otra, { otra = it }, "Nueva categoría")
            }
        },
        confirmButton = {
            TextButton(
                enabled = otra.isNotBlank(),
                onClick = {
                    val nueva = otra.trim().replaceFirstChar { it.uppercase() }
                    // Si ya existe (sin importar mayúsculas) se usa la existente, para no duplicar
                    onElegir(categorias.firstOrNull { it.equals(nueva, ignoreCase = true) } ?: nueva)
                }
            ) { Text("Usar nueva") }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text("Cancelar") } }
    )
}