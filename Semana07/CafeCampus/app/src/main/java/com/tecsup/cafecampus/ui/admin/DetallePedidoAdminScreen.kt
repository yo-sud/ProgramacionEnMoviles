package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.ui.etiquetaPago
import com.tecsup.cafecampus.ui.fechaHora
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.ui.theme.EstadoPreparando
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel

@Composable
fun DetallePedidoAdminScreen(vm: PedidosAdminViewModel, pedidoId: Int, onVolver: () -> Unit) {
    val pedido by remember(pedidoId) { vm.pedido(pedidoId) }.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Pedido #$pedidoId", onVolver = onVolver)

        val p = pedido
        if (p == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            val estado = EstadoPedido.desde(p.pedido.estado)
            val siguiente = estado.siguiente()

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cliente
                CafeCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(p.cliente.nombre, style = MaterialTheme.typography.titleMedium)
                        EstadoChip(estado)
                    }
                    Text(p.cliente.correo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Recojo: ${p.pedido.horaRecojo} · ${p.pedido.pago.etiquetaPago()}")
                    Text(p.pedido.fecha.fechaHora(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                // Líneas y total
                CafeCard {
                    Text("Productos", style = MaterialTheme.typography.titleMedium)
                    p.detalles.forEach { d ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${d.cantidad} × ${d.nombreProducto}", modifier = Modifier.weight(1f))
                            Text((d.cantidad * d.precioUnitario).soles())
                        }
                    }
                    HorizontalDivider()
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text(p.pedido.total.soles(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }

                // Nota del cliente (tarjeta ámbar)
                if (p.pedido.nota.isNotBlank()) {
                    CafeCard(colorFondo = EstadoPreparando.copy(alpha = 0.15f)) {
                        Text("Nota del cliente", style = MaterialTheme.typography.titleMedium)
                        Text(p.pedido.nota)
                    }
                }

                // Selector de estado
                CafeCard {
                    Text("Estado del pedido", style = MaterialTheme.typography.titleMedium)
                    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        EstadoPedido.entries.filter { it != EstadoPedido.CANCELADO }.forEach { e ->
                            FilterChip(
                                selected = estado == e,
                                enabled = estado != EstadoPedido.CANCELADO,
                                onClick = { vm.cambiarA(p, e) },
                                label = { Text(e.etiqueta) }
                            )
                        }
                    }
                }

                if (siguiente != null) {
                    CafeButton("Pasar a ${siguiente.etiqueta}", onClick = { vm.avanzar(p) })
                }
                if (estado.esActivo) {
                    CafeButton("Cancelar pedido", secundario = true, onClick = { vm.cancelar(p) })
                }
            }
        }
    }
}