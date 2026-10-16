package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.PedidoCard
import com.tecsup.cafecampus.ui.etiquetaPago
import com.tecsup.cafecampus.ui.resumenLineas
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.ui.theme.EstadoListo
import com.tecsup.cafecampus.ui.theme.EstadoPreparando
import com.tecsup.cafecampus.ui.theme.EstadoRecibido
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel

@Composable
fun PanelScreen(vm: PanelAdminViewModel, onVerPedidos: () -> Unit, onSalir: () -> Unit = {}) {
    val nuevos by vm.nuevos.collectAsStateWithLifecycle()
    val preparando by vm.preparando.collectAsStateWithLifecycle()
    val listos by vm.listos.collectAsStateWithLifecycle()
    val ventas by vm.ventasHoy.collectAsStateWithLifecycle()
    val nuevosPedidos by vm.nuevosPedidos.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Panel", acciones = {
            IconButton(onClick = onSalir) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
            }
        })

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Contador(nuevos, "Nuevos", EstadoRecibido, Modifier.weight(1f))
                    Contador(preparando, "Preparando", EstadoPreparando, Modifier.weight(1f))
                    Contador(listos, "Listos", EstadoListo, Modifier.weight(1f))
                }
            }
            item {
                CafeCard {
                    Text("Ventas de hoy", style = MaterialTheme.typography.titleMedium)
                    Text(ventas.soles(), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                    Text("No incluye pedidos cancelados", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Nuevos pedidos", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = onVerPedidos) { Text("Ver todos") }
                }
            }
            if (nuevosPedidos.isEmpty()) {
                item { Text("No hay pedidos nuevos", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            items(nuevosPedidos.take(3), key = { it.pedido.id }) { p ->
                PedidoCard(
                    titulo = "Pedido #${p.pedido.id}",
                    estado = EstadoPedido.desde(p.pedido.estado),
                    lineas = p.resumenLineas(),
                    total = p.pedido.total,
                    subtitulo = "${p.cliente.nombre} · Recojo ${p.pedido.horaRecojo} · ${p.pedido.pago.etiquetaPago()}",
                    acciones = { CafeButton("Preparar", onClick = { vm.preparar(p) }) }
                )
            }
        }
    }
}

@Composable
private fun Contador(valor: Int, etiqueta: String, color: Color, modifier: Modifier = Modifier) {
    CafeCard(modifier = modifier, colorFondo = color.copy(alpha = 0.12f), espaciado = 2) {
        Text("$valor", style = MaterialTheme.typography.headlineMedium, color = color)
        Text(etiqueta, style = MaterialTheme.typography.labelMedium)
    }
}