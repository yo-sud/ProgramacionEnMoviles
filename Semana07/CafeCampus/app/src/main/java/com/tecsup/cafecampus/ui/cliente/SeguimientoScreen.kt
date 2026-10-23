package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.StepperVertical
import com.tecsup.cafecampus.ui.numeroPedido
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.ui.textoRecojo
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

/** Se actualiza SOLO cuando el administrador cambia el estado: observa un Flow de Room. */
@Composable
fun SeguimientoScreen(vm: PedidosClienteViewModel, pedidoId: Int, onVolver: () -> Unit) {
    val pedido by remember(pedidoId) { vm.pedido(pedidoId) }.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Seguimiento", onVolver = onVolver, centrado = true)

        val p = pedido
        if (p == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            val estado = EstadoPedido.desde(p.pedido.estado)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                CabeceraPedido(
                    numero = p.pedido.id.numeroPedido(),
                    recojo = p.pedido.horaRecojo.textoRecojo(),
                    estado = estado
                )
                if (estado == EstadoPedido.CANCELADO) {
                    CafeCard(colorFondo = MaterialTheme.colorScheme.errorContainer) {
                        Text("Este pedido fue cancelado", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onErrorContainer)
                        Text("Si tienes dudas, acércate a la caja de la cafetería.", color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                } else {
                    StepperVertical(estado, Modifier.padding(horizontal = 8.dp))
                }
            }

            // Resumen fijo al pie, sobre la barra inferior
            CafeCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), espaciado = 6) {
                Text(
                    p.detalles.joinToString("  ·  ") { "${it.cantidad} x ${it.nombreProducto}" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                if (p.pedido.nota.isNotBlank()) {
                    Text(
                        "Nota: ${p.pedido.nota}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(p.pedido.total.soles(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

/** Tarjeta de color del perfil (roja si el pedido fue cancelado) con el estado de la espera. */
@Composable
private fun CabeceraPedido(numero: String, recojo: String, estado: EstadoPedido) {
    val c = MaterialTheme.colorScheme
    val cancelado = estado == EstadoPedido.CANCELADO
    val fondo = if (cancelado) c.error else c.primary
    val texto = if (cancelado) c.onError else c.onPrimary

    val tiempo = estado.minutosEstimados?.let { "Tiempo estimado: $it min" } ?: when (estado) {
        EstadoPedido.LISTO -> "¡Listo para recoger!"
        EstadoPedido.ENTREGADO -> "Pedido entregado"
        else -> "Pedido cancelado"
    }
    val icono = when (estado) {
        EstadoPedido.RECIBIDO, EstadoPedido.PREPARANDO -> Icons.Outlined.Schedule
        EstadoPedido.LISTO, EstadoPedido.ENTREGADO -> Icons.Outlined.CheckCircle
        EstadoPedido.CANCELADO -> Icons.Outlined.Cancel
    }

    Surface(modifier = Modifier.fillMaxWidth(), color = fondo, shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Pedido $numero", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = texto)
                Text(recojo, color = texto.copy(alpha = 0.92f))
                Text(tiempo, fontWeight = FontWeight.Bold, color = texto)
            }
            Icon(icono, contentDescription = null, tint = texto, modifier = Modifier.size(44.dp))
        }
    }
}