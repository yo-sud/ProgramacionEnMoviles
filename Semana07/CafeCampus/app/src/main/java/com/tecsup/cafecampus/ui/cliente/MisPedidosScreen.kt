package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.ui.components.SelectorSegmentado
import com.tecsup.cafecampus.ui.fechaRelativa
import com.tecsup.cafecampus.ui.numeroPedido
import com.tecsup.cafecampus.ui.resumenCorto
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

@Composable
fun MisPedidosScreen(
    vm: PedidosClienteViewModel,
    onPedido: (Int) -> Unit,
    onRepetir: (PedidoConDetalle) -> Unit = {},
    onVolver: (() -> Unit)? = null
) {
    val activos by vm.activos.collectAsStateWithLifecycle()
    val historial by vm.historial.collectAsStateWithLifecycle()
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val lista = if (tab == 0) activos else historial

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Mis pedidos", onVolver = onVolver, centrado = true)

        SelectorSegmentado(
            opciones = listOf("Activos", "Historial"),
            seleccion = tab,
            onSeleccion = { tab = it },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (lista.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    if (tab == 0) "No tienes pedidos activos" else "Aún no tienes historial",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista, key = { it.pedido.id }) { p ->
                    TarjetaPedido(
                        p = p,
                        mostrarRepetir = tab == 1, // solo en el Historial
                        onClick = { onPedido(p.pedido.id) },
                        onRepetir = { onRepetir(p) }
                    )
                }
            }
        }
    }
}

/** Tarjeta del listado: número, resumen, fecha y estado a la izquierda; flecha y total a la derecha. */
@Composable
private fun TarjetaPedido(
    p: PedidoConDetalle,
    mostrarRepetir: Boolean,
    onClick: () -> Unit,
    onRepetir: () -> Unit
) {
    val c = MaterialTheme.colorScheme
    val estado = EstadoPedido.desde(p.pedido.estado)

    CafeCard(onClick = onClick, espaciado = 10) {
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Pedido ${p.pedido.id.numeroPedido()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    p.resumenCorto(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = c.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    p.pedido.fecha.fechaRelativa(),
                    style = MaterialTheme.typography.bodySmall,
                    color = c.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(4.dp))
                EstadoChip(estado)
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = c.onSurfaceVariant)
                Text(p.pedido.total.soles(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = c.primary)
            }
        }
        if (mostrarRepetir) {
            OutlinedButton(
                onClick = onRepetir,
                modifier = Modifier.align(Alignment.End).height(36.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, c.primary),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
            ) {
                Text("Repetir pedido", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}