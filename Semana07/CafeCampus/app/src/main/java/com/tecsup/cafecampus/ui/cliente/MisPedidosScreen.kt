package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.PedidoCard
import com.tecsup.cafecampus.ui.etiquetaPago
import com.tecsup.cafecampus.ui.resumenLineas
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

@Composable
fun MisPedidosScreen(
    vm: PedidosClienteViewModel,
    onPedido: (Int) -> Unit,
    onRepetir: (PedidoConDetalle) -> Unit = {}
) {
    val activos by vm.activos.collectAsStateWithLifecycle()
    val historial by vm.historial.collectAsStateWithLifecycle()
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val lista = if (tab == 0) activos else historial

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Mis pedidos")
        TabRow(selectedTabIndex = tab) {
            Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Activos (${activos.size})") })
            Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Historial (${historial.size})") })
        }

        if (lista.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    if (tab == 0) "No tienes pedidos activos" else "Aún no tienes historial",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista, key = { it.pedido.id }) { p ->
                    PedidoCard(
                        titulo = "Pedido #${p.pedido.id}",
                        estado = EstadoPedido.desde(p.pedido.estado),
                        lineas = p.resumenLineas(),
                        total = p.pedido.total,
                        subtitulo = "Recojo: ${p.pedido.horaRecojo} · ${p.pedido.pago.etiquetaPago()}",
                        onClick = { onPedido(p.pedido.id) },
                        acciones = {
                            // Solo en el Historial
                            if (tab == 1) {
                                CafeButton("Repetir pedido", secundario = true, onClick = { onRepetir(p) })
                            }
                        }
                    )
                }
            }
        }
    }
}