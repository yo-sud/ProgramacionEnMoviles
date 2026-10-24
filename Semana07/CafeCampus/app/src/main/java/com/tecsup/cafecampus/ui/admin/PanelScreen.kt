package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.PedidosPorHora
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.horaCorta
import com.tecsup.cafecampus.ui.nombreCorto
import com.tecsup.cafecampus.ui.numeroPedido
import com.tecsup.cafecampus.ui.resumenAdmin
import com.tecsup.cafecampus.ui.soles
import com.tecsup.cafecampus.ui.theme.EstadoListo
import com.tecsup.cafecampus.ui.theme.EstadoPreparando
import com.tecsup.cafecampus.ui.theme.EstadoRecibido
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel

@Composable
fun PanelScreen(vm: PanelAdminViewModel, onVerPedidos: () -> Unit, mostrarAvisos: Boolean = true) {    val nuevos by vm.nuevos.collectAsStateWithLifecycle()
    val preparando by vm.preparando.collectAsStateWithLifecycle()
    val listos by vm.listos.collectAsStateWithLifecycle()
    val ventas by vm.ventasHoy.collectAsStateWithLifecycle()
    val pedidosHoy by vm.pedidosHoy.collectAsStateWithLifecycle()
    val porHora by vm.pedidosPorHora.collectAsStateWithLifecycle()
    val nuevosPedidos by vm.nuevosPedidos.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { Encabezado(nuevos = if (mostrarAvisos) nuevos else 0, onCampana = onVerPedidos) }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Contador(nuevos, "Nuevos", EstadoRecibido, Modifier.weight(1f))
                Contador(preparando, "Preparando", EstadoPreparando, Modifier.weight(1f))
                Contador(listos, "Listos", EstadoListo, Modifier.weight(1f))
            }
        }

        item { TarjetaVentas(ventas = ventas, pedidos = pedidosHoy) }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Pedidos por hora", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                GraficoPorHora(porHora)
            }
        }

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Nuevos pedidos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                TextButton(onClick = onVerPedidos) {
                    Text("Ver todos", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (nuevosPedidos.isEmpty()) {
            item { Text("No hay pedidos nuevos", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
        items(nuevosPedidos.take(3), key = { it.pedido.id }) { p ->
            TarjetaNuevo(p = p, onPreparar = { vm.preparar(p) })
        }
    }
}

@Composable
private fun Encabezado(nuevos: Int, onCampana: () -> Unit) {
    val c = MaterialTheme.colorScheme
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                "Panel de control",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text("Cafetería · Hoy", color = c.onSurfaceVariant)
        }
        // La campana avisa cuántos pedidos nuevos hay y abre la lista de pedidos
        IconButton(onClick = onCampana) {
            BadgedBox(badge = {
                if (nuevos > 0) Badge(containerColor = c.primary, contentColor = c.onPrimary) { Text("$nuevos") }
            }) {
                Icon(Icons.Filled.Notifications, contentDescription = "Ver pedidos nuevos", modifier = Modifier.size(28.dp))
            }
        }
    }
}

@Composable
private fun Contador(valor: Int, etiqueta: String, color: Color, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = color.copy(alpha = 0.14f), shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("$valor", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = color)
            Text(etiqueta, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun TarjetaVentas(ventas: Double, pedidos: Int) {
    val c = MaterialTheme.colorScheme
    Surface(modifier = Modifier.fillMaxWidth(), color = c.primary, shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Ventas del día", color = c.onPrimary.copy(alpha = 0.85f))
                Text(ventas.soles(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = c.onPrimary)
            }
            Text(
                if (pedidos == 1) "1 pedido" else "$pedidos pedidos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = c.onPrimary
            )
        }
    }
}

/**
 * Gráfico de barras sin librerías: altura proporcional a los pedidos de cada hora,
 * con la hora de mayor demanda resaltada. Si hay muchas horas, se desplaza a los lados.
 */
@Composable
private fun GraficoPorHora(datos: List<PedidosPorHora>) {
    val c = MaterialTheme.colorScheme
    val maximo = datos.maxOfOrNull { it.cantidad } ?: 0

    CafeCard {
        if (maximo == 0) {
            Box(Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
                Text("Aún no hay pedidos hoy", color = c.onSurfaceVariant)
            }
        } else {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val anchoColumna = maxOf(maxWidth / datos.size, 48.dp)
                Row(Modifier.horizontalScroll(rememberScrollState()), verticalAlignment = Alignment.Bottom) {
                    datos.forEach { d ->
                        val esPico = d.cantidad == maximo
                        val alto = if (d.cantidad == 0) 4.dp else maxOf(12.dp, 100.dp * (d.cantidad.toFloat() / maximo))
                        Column(
                            modifier = Modifier.width(anchoColumna),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (d.cantidad > 0) {
                                Text("${d.cantidad}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(4.dp))
                            Box(
                                Modifier
                                    .width(36.dp)
                                    .height(alto)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(
                                        when {
                                            d.cantidad == 0 -> c.outlineVariant
                                            esPico -> c.primary
                                            else -> c.primary.copy(alpha = 0.3f)
                                        }
                                    )
                            )
                            Spacer(Modifier.height(6.dp))
                            Text("${d.hora}h", style = MaterialTheme.typography.labelMedium, color = c.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

/** "#0047  Ana T." con su resumen y la hora de recojo; el botón pasa el pedido a Preparando. */
@Composable
private fun TarjetaNuevo(p: PedidoConDetalle, onPreparar: () -> Unit) {
    val c = MaterialTheme.colorScheme
    CafeCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "${p.pedido.id.numeroPedido()}  ${p.cliente.nombre.nombreCorto()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${p.resumenAdmin()} · ${p.pedido.horaRecojo.horaCorta()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = c.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Button(
                onClick = onPreparar,
                modifier = Modifier.height(44.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                Text("Preparar", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}