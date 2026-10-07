package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.DatosCliente
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto

private const val COSTO_DELIVERY = 4.00

@Composable
fun DatosEntregaScreen(
    carrito: List<ItemCarrito>,
    cliente: DatosCliente,
    onVolver: () -> Unit,
    onConfirmarPedido: (DatosCliente) -> Unit
) {
    var nombre by remember { mutableStateOf(cliente.nombre) }
    var telefono by remember { mutableStateOf(cliente.telefono) }
    var direccion by remember { mutableStateOf(cliente.direccion) }
    var referencia by remember { mutableStateOf(cliente.referencia) }

    val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
    val total = subtotal + COSTO_DELIVERY

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text(text = "Datos de entrega", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(Modifier.height(16.dp))
        CampoTexto(
            etiqueta = "Nombre completo",
            valor = nombre,
            onValorCambia = { nombre = it },
            placeholder = "Juan Pérez"
        )
        Spacer(Modifier.height(16.dp))
        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { telefono = it },
            placeholder = "987 654 321",
            teclado = KeyboardType.Phone
        )
        Spacer(Modifier.height(16.dp))
        CampoTexto(
            etiqueta = "Dirección",
            valor = direccion,
            onValorCambia = { direccion = it },
            placeholder = "Av. Los Olivos 123"
        )
        Spacer(Modifier.height(16.dp))
        CampoTexto(
            etiqueta = "Referencia",
            valor = referencia,
            onValorCambia = { referencia = it },
            placeholder = "Frente al parque"
        )
        Spacer(Modifier.height(16.dp))
        Text(text = "Resumen del pedido", style = MaterialTheme.typography.titleMedium)
        FilaResumen(etiqueta = "Subtotal", valor = subtotal)
        FilaResumen(etiqueta = "Costo de delivery", valor = COSTO_DELIVERY)
        HorizontalDivider()
        FilaResumen(etiqueta = "Total", valor = total)
        Spacer(Modifier.height(16.dp))
        BotonPrimario(
            texto = "Confirmar pedido",
            onClick = {
                onConfirmarPedido(DatosCliente(nombre, telefono, direccion, referencia))
            }
        )
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta)
        Text(text = "S/ %.2f".format(valor))
    }
}