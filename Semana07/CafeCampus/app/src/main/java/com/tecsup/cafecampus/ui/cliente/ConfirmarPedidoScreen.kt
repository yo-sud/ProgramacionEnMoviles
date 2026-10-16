package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

private val HORAS = listOf("Lo antes posible", "12:30", "12:45", "13:00", "13:15", "13:30")

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

    var hora by rememberSaveable { mutableStateOf(HORAS.first()) }
    var pago by rememberSaveable { mutableStateOf("CAJA") }
    var nota by rememberSaveable { mutableStateOf("") }
    val total = lineas.sumOf { it.subtotal }

    LaunchedEffect(Unit) {
        pedidosVm.pedidoCreado.collect { id ->
            menuVm.vaciar()
            onPedidoCreado(id.toInt())
        }
    }

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Confirmar pedido", onVolver = onVolver)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!abierto) {
                item {
                    CafeCard(colorFondo = MaterialTheme.colorScheme.errorContainer) {
                        Text("Cafetería cerrada", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onErrorContainer)
                        Text("Por ahora no se reciben pedidos. Intenta más tarde.", color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }
            if (lineas.isEmpty()) {
                item { Text("Tu bolsa está vacía. Agrega productos desde el menú.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            items(lineas, key = { it.producto.id }) { l ->
                CafeCard {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("${l.cantidad} × ${l.producto.nombre}", style = MaterialTheme.typography.titleMedium)
                            if (l.nota.isNotBlank()) Text("Nota: ${l.nota}", style = MaterialTheme.typography.bodySmall)
                        }
                        Text(l.subtotal.soles(), color = MaterialTheme.colorScheme.primary)
                        IconButton(onClick = { menuVm.quitar(l.producto.id) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Quitar")
                        }
                    }
                }
            }
            item {
                Text("Hora de recojo", style = MaterialTheme.typography.titleMedium)
                Row(
                    Modifier.horizontalScroll(rememberScrollState()).padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HORAS.forEach { h ->
                        FilterChip(selected = hora == h, onClick = { hora = h }, label = { Text(h) })
                    }
                }
            }
            item {
                Text("Forma de pago", style = MaterialTheme.typography.titleMedium)
                Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = pago == "CAJA", onClick = { pago = "CAJA" }, label = { Text("Caja") })
                    FilterChip(selected = pago == "YAPE", onClick = { pago = "YAPE" }, label = { Text("Yape / Plin") })
                }
            }
            item {
                CafeTextField(
                    valor = nota, onValor = { nota = it },
                    etiqueta = "Nota para el pedido (opcional)",
                    singleLine = false, minLineas = 2
                )
            }
            item {
                CafeCard {
                    FilaMonto("Subtotal", total.soles(), negrita = false)
                    FilaMonto("Total", total.soles(), negrita = true)
                }
            }
        }

        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            CafeButton(
                texto = "Confirmar pedido",
                enabled = lineas.isNotEmpty() && abierto && !enviando,
                onClick = { pedidosVm.confirmar(lineas, hora, pago, nota.trim()) }
            )
            CafeButton(texto = "Seguir pidiendo", secundario = true, onClick = onSeguirPidiendo)
        }
    }
}

@Composable
private fun FilaMonto(etiqueta: String, valor: String, negrita: Boolean) {
    val peso = if (negrita) FontWeight.Bold else FontWeight.Normal
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, fontWeight = peso)
        Text(valor, fontWeight = peso, color = if (negrita) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
    }
}