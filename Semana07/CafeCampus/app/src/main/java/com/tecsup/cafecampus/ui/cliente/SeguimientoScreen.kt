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
import androidx.compose.material3.CircularProgressIndicator
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
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.ui.components.StepperEstado
import com.tecsup.cafecampus.ui.etiquetaPago
import com.tecsup.cafecampus.ui.fechaHora
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

private fun mensajeDe(e: EstadoPedido) = when (e) {
    EstadoPedido.RECIBIDO -> "Recibimos tu pedido. Pronto empezaremos a prepararlo."
    EstadoPedido.PREPARANDO -> "Estamos preparando tu pedido."
    EstadoPedido.LISTO -> "¡Tu pedido está listo! Pasa a recogerlo."
    EstadoPedido.ENTREGADO -> "Pedido entregado. ¡Gracias!"
    EstadoPedido.CANCELADO -> "Este pedido fue cancelado."
}

/** Se actualiza SOLO cuando el administrador cambia el estado: observa un Flow de Room. */
@Composable
fun SeguimientoScreen(vm: PedidosClienteViewModel, pedidoId: Int, onVolver: () -> Unit) {
    val pedido by remember(pedidoId) { vm.pedido(pedidoId) }.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Seguimiento", onVolver = onVolver)

        val p = pedido
        if (p == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            val estado = EstadoPedido.desde(p.pedido.estado)
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CafeCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Pedido #${p.pedido.id}", style = MaterialTheme.typography.titleLarge)
                        EstadoChip(estado)
                    }
                    Text(mensajeDe(estado))
                    Text("Recojo: ${p.pedido.horaRecojo} · ${p.pedido.pago.etiquetaPago()}", style = MaterialTheme.typography.bodyMedium)
                    Text(p.pedido.fecha.fechaHora(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                CafeCard(espaciado = 12) {
                    Text("Estado del pedido", style = MaterialTheme.typography.titleMedium)
                    StepperEstado(estado)
                }

                CafeCard {
                    Text("Detalle", style = MaterialTheme.typography.titleMedium)
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
                    if (p.pedido.nota.isNotBlank()) Text("Nota: ${p.pedido.nota}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}