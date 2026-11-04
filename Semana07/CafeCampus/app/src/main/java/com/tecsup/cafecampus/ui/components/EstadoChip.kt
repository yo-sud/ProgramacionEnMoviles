package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.theme.*

@Composable
fun EstadoChip(estado: String) {
    val (texto, color) = when (estado) {
        EstadoPedido.RECIBIDO.name -> "Recibido" to EstadoRecibido
        EstadoPedido.PREPARANDO.name -> "Preparando" to EstadoPreparando
        EstadoPedido.LISTO.name -> "Listo" to EstadoListo
        EstadoPedido.ENTREGADO.name -> "Entregado" to EstadoListo
        EstadoPedido.CANCELADO.name -> "Cancelado" to EstadoCancelado
        else -> estado to Color.Gray
    }
    EtiquetaChip(texto, color)
}

@Composable
fun EstadoChip(estado: EstadoPedido) {
    EstadoChip(estado.name)
}

@Composable
fun EtiquetaChip(texto: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = texto,
            color = color,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
