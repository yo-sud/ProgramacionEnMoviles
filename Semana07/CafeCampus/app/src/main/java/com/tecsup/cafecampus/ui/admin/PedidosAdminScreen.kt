package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.CafeChip
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.ui.numeroPedido
import com.tecsup.cafecampus.ui.recojoCorto
import com.tecsup.cafecampus.ui.resumenCompleto
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.ui.theme.EstadoCancelado
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel

private fun EstadoPedido.nombreFiltro(): String = when (this) {
    EstadoPedido.RECIBIDO -> "Nuevos"
    EstadoPedido.PREPARANDO -> "Preparando"
    EstadoPedido.LISTO -> "Listos"
    EstadoPedido.ENTREGADO -> "Entregados"
    EstadoPedido.CANCELADO -> "Cancelados"
}

private fun EstadoPedido.mensajeVacio(): String = when (this) {
    EstadoPedido.RECIBIDO -> "No hay pedidos nuevos"
    EstadoPedido.PREPARANDO -> "No hay pedidos en preparación"
    EstadoPedido.LISTO -> "No hay pedidos listos"
    EstadoPedido.ENTREGADO -> "No hay pedidos entregados"
    EstadoPedido.CANCELADO -> "No hay pedidos cancelados"
}

@Composable
fun PedidosAdminScreen(
    vm: PedidosAdminViewModel,
    onDetalle: (Int) -> Unit,
    onVolver: (() -> Unit)? = null
) {
    val seleccionado by vm.estadoSeleccionado.collectAsStateWithLifecycle()
    val pedidos by vm.pedidos.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Pedidos", onVolver = onVolver, centrado = true)

        // Nuevos, Preparando y Listos como en el diseño; Entregados y Cancelados quedan al desplazar
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(EstadoPedido.entries) { e ->
                CafeChip(e.nombreFiltro(), seleccionado == e, onClick = { vm.seleccionar(e) })
            }
        }

        if (pedidos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(seleccionado.mensajeVacio(), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos, key = { it.pedido.id }) { p ->
                    TarjetaPedidoAdmin(
                        p = p,
                        onClick = { onDetalle(p.pedido.id) },
                        onAvanzar = { vm.avanzar(p) },
                        onCancelar = { vm.cancelar(p) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaPedidoAdmin(
    p: PedidoConDetalle,
    onClick: () -> Unit,
    onAvanzar: () -> Unit,
    onCancelar: () -> Unit
) {
    val c = MaterialTheme.colorScheme
    val estado = EstadoPedido.desde(p.pedido.estado)
    val siguiente = estado.siguiente()

    // "Recojo 10:45  ·  S/ 9.00  ·  Nota: sin tomate" (la nota solo aparece si existe)
    val meta = buildList {
        add(p.pedido.horaRecojo.recojoCorto())
        add(p.pedido.total.soles())
        if (p.pedido.nota.isNotBlank()) add("Nota: ${p.pedido.nota}")
    }.joinToString("  ·  ")

    CafeCard(onClick = onClick, espaciado = 10) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                Modifier.size(40.dp).clip(CircleShape).background(c.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Person, contentDescription = null, tint = c.primary, modifier = Modifier.size(22.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "${p.pedido.id.numeroPedido()}  ${p.cliente.nombre}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    p.resumenCompleto(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = c.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            EstadoChip(estado)
        }

        Text(
            meta,
            style = MaterialTheme.typography.bodySmall,
            color = c.onSurfaceVariant.copy(alpha = 0.8f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Solo los pedidos activos tienen siguiente estado y se pueden cancelar
        if (siguiente != null) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onAvanzar,
                    modifier = Modifier.weight(1.4f).height(40.dp),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text("Pasar a ${siguiente.etiqueta}", fontWeight = FontWeight.SemiBold, maxLines = 1)
                }
                Button(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f).height(40.dp),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EstadoCancelado.copy(alpha = 0.12f),
                        contentColor = EstadoCancelado
                    )
                ) {
                    Text("Cancelar", fontWeight = FontWeight.SemiBold, maxLines = 1)
                }
            }
        }
    }
}