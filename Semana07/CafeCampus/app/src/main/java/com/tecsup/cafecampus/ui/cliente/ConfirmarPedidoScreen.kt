package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.ui.components.ProductoIcono
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

/** Cargo por servicio. Hoy es 0: se muestra como en el diseño, pero no se guarda en el pedido. */
private const val CARGO_SERVICIO = 0.0

@Composable
fun ConfirmarPedidoScreen(
    menuVm: MenuViewModel,
    pedidosVm: PedidosClienteViewModel,
    sesionVm: SesionViewModel,
    onSeguirPidiendo: () -> Unit,
    onPedidoCreado: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val lineas by menuVm.carrito.collectAsStateWithLifecycle()
    val abierto by sesionVm.recibirPedidos.collectAsStateWithLifecycle()
    val enviando by pedidosVm.enviando.collectAsStateWithLifecycle()
    val error by pedidosVm.error.collectAsStateWithLifecycle()
    val colores = MaterialTheme.colorScheme

    // Los horarios salen del ViewModel y se calculan al abrir la pantalla
    val opciones = remember { pedidosVm.opcionesHora() }
    var hora by rememberSaveable { mutableStateOf(opciones.first()) }
    var pago by rememberSaveable { mutableStateOf("CAJA") } // CAJA | YAPE
    var nota by rememberSaveable { mutableStateOf("") }
    val horaElegida = if (hora in opciones) hora else opciones.first()

    val subtotal = lineas.sumOf { it.subtotal }
    val total = subtotal + CARGO_SERVICIO

    // Cuando el pedido se crea: vaciar el carrito y navegar a su seguimiento
    LaunchedEffect(Unit) {
        pedidosVm.pedidoCreado.collect { id ->
            menuVm.vaciar()
            onPedidoCreado(id.toInt())
        }
    }

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Confirmar pedido", onVolver = onVolver, centrado = true)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (!abierto) {
                item {
                    CafeCard(colorFondo = colores.errorContainer) {
                        Text("Cafetería cerrada", style = MaterialTheme.typography.titleMedium, color = colores.onErrorContainer)
                        Text("Por ahora no se reciben pedidos. Intenta más tarde.", color = colores.onErrorContainer)
                    }
                }
            }

            if (lineas.isEmpty()) {
                item { Text("Tu bolsa está vacía. Agrega productos desde el menú.", color = colores.onSurfaceVariant) }
            } else {
                item {
                    CafeCard(espaciado = 12) {
                        lineas.forEachIndexed { i, l ->
                            if (i > 0) HorizontalDivider(color = colores.outlineVariant)
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                ProductoIcono(l.producto.icono, tamano = 48.dp)
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(l.producto.nombre, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text("x${l.cantidad}", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                                    if (l.nota.isNotBlank()) {
                                        Text("Nota: ${l.nota}", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                                Text(l.subtotal.soles(), fontWeight = FontWeight.Bold)
                                IconButton(onClick = { menuVm.quitar(l.producto.id) }, modifier = Modifier.size(32.dp)) {
                                    Icon(
                                        Icons.Outlined.Delete,
                                        contentDescription = "Quitar ${l.producto.nombre}",
                                        tint = colores.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Seccion("Hora de recojo") {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        opciones.forEach { h ->
                            OpcionHora(h, activa = horaElegida == h, modifier = Modifier.weight(1f)) { hora = h }
                        }
                    }
                }
            }

            item {
                Seccion("Forma de pago") {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OpcionPago("Pagar en caja", activa = pago == "CAJA", modifier = Modifier.weight(1f)) { pago = "CAJA" }
                        OpcionPago("Yape / Plin", activa = pago == "YAPE", modifier = Modifier.weight(1f)) { pago = "YAPE" }
                    }
                }
            }

            item {
                CafeTextField(
                    valor = nota, onValor = { nota = it },
                    etiqueta = "Nota para la cafetería (opcional)",
                    singleLine = false, minLineas = 2
                )
            }

            if (lineas.isNotEmpty()) {
                item {
                    CafeCard(espaciado = 10) {
                        FilaMonto("Subtotal", subtotal.soles())
                        FilaMonto("Servicio", CARGO_SERVICIO.soles())
                        HorizontalDivider(color = colores.outlineVariant)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(total.soles(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colores.primary)
                        }
                    }
                }
            }
        }

        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            error?.let { Text(it, color = colores.error) }
            CafeButton(
                texto = "Confirmar pedido",
                enabled = lineas.isNotEmpty() && abierto && !enviando,
                onClick = { pedidosVm.confirmar(lineas, horaElegida, pago, nota.trim()) }
            )
            CafeButton(texto = "Seguir pidiendo", secundario = true, onClick = onSeguirPidiendo)
        }
    }
}

@Composable
private fun Seccion(titulo: String, contenido: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        contenido()
    }
}

@Composable
private fun FilaMonto(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, fontWeight = FontWeight.Bold)
    }
}

/** Horario de recojo: píldora rellena con el color del perfil cuando está activa. */
@Composable
private fun OpcionHora(texto: String, activa: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val c = MaterialTheme.colorScheme
    Opcion(
        texto = texto,
        modifier = modifier,
        forma = RoundedCornerShape(50),
        fondo = if (activa) c.primary else c.surfaceContainerLowest,
        borde = if (activa) null else BorderStroke(1.dp, c.outlineVariant),
        colorTexto = if (activa) c.onPrimary else c.onSurface,
        activa = activa,
        onClick = onClick
    )
}

/** Forma de pago: caja suave con borde del color del perfil cuando está activa. */
@Composable
private fun OpcionPago(texto: String, activa: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val c = MaterialTheme.colorScheme
    Opcion(
        texto = texto,
        modifier = modifier,
        forma = RoundedCornerShape(12.dp),
        fondo = if (activa) c.primaryContainer else c.surfaceContainerLowest,
        borde = BorderStroke(if (activa) 1.5.dp else 1.dp, if (activa) c.primary else c.outlineVariant),
        colorTexto = if (activa) c.primary else c.onSurfaceVariant,
        activa = activa,
        onClick = onClick
    )
}

@Composable
private fun Opcion(
    texto: String,
    modifier: Modifier,
    forma: Shape,
    fondo: Color,
    borde: BorderStroke?,
    colorTexto: Color,
    activa: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(forma)
            .background(fondo)
            .then(if (borde != null) Modifier.border(borde, forma) else Modifier)
            .selectable(selected = activa, onClick = onClick, role = Role.RadioButton),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, color = colorTexto, fontWeight = FontWeight.SemiBold, maxLines = 1, style = MaterialTheme.typography.labelLarge)
    }
}