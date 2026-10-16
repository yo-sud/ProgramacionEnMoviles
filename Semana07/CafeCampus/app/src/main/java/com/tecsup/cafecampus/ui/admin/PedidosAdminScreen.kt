package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.PedidoCard
import com.tecsup.cafecampus.ui.etiquetaPago
import com.tecsup.cafecampus.ui.resumenLineas
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel

@Composable
fun PedidosAdminScreen(vm: PedidosAdminViewModel, onDetalle: (Int) -> Unit) {
    val seleccionado by vm.estadoSeleccionado.collectAsStateWithLifecycle()
    val pedidos by vm.pedidos.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Pedidos")

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(EstadoPedido.entries) { e ->
                FilterChip(selected = seleccionado == e, onClick = { vm.seleccionar(e) }, label = { Text(e.etiqueta) })
            }
        }

        if (pedidos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay pedidos en estado ${seleccionado.etiqueta}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos, key = { it.pedido.id }) { p ->
                    val estado = EstadoPedido.desde(p.pedido.estado)
                    val siguiente = estado.siguiente()
                    PedidoCard(
                        titulo = "Pedido #${p.pedido.id}",
                        estado = estado,
                        lineas = p.resumenLineas(),
                        total = p.pedido.total,
                        subtitulo = "${p.cliente.nombre} · Recojo ${p.pedido.horaRecojo} · ${p.pedido.pago.etiquetaPago()}",
                        nota = p.pedido.nota,
                        onClick = { onDetalle(p.pedido.id) },
                        acciones = {
                            if (siguiente != null) {
                                CafeButton("Pasar a ${siguiente.etiqueta}", onClick = { vm.avanzar(p) })
                            }
                            if (estado.esActivo) {
                                CafeButton("Cancelar", secundario = true, onClick = { vm.cancelar(p) })
                            }
                        }
                    )
                }
            }
        }
    }
}