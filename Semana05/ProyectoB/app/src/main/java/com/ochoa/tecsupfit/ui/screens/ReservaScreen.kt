package com.ochoa.tecsupfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ochoa.tecsupfit.model.Reserva

@Composable
fun ReservasScreen(
    reservas: List<Reserva>,
    onCancelarReserva: (Reserva) -> Unit,
    modifier: Modifier = Modifier
) {
    var reservaACancelar by remember { mutableStateOf<Reserva?>(null) }

    if (reservas.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Aún no tienes reservas", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(reservas) { reserva ->
            TarjetaReserva(
                reserva = reserva,
                onCancelarClick = { reservaACancelar = reserva }
            )
        }
    }

    // AlertDialog de confirmacion antes de cancelar
    reservaACancelar?.let { reserva ->
        AlertDialog(
            onDismissRequest = { reservaACancelar = null },
            title = { Text("Cancelar reserva") },
            text = {
                Text("¿Seguro que deseas cancelar tu reserva de \"${reserva.clase.nombre}\" a las ${reserva.horarioElegido}?")
            },
            confirmButton = {
                TextButton(onClick = {
                    onCancelarReserva(reserva)
                    reservaACancelar = null
                }) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { reservaACancelar = null }) {
                    Text("Volver")
                }
            }
        )
    }
}

@Composable
private fun TarjetaReserva(
    reserva: Reserva,
    onCancelarClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = reserva.clase.nombre,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Horario: ${reserva.horarioElegido}", style = MaterialTheme.typography.bodyMedium)
                }

                val colorEstado = if (reserva.estado == "Confirmada") Color(0xFF2E7D32) else Color(0xFF757575)
                AssistChip(
                    onClick = { },
                    label = { Text(reserva.estado) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = colorEstado.copy(alpha = 0.15f),
                        labelColor = colorEstado
                    )
                )
            }

            if (reserva.estado == "Confirmada") {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = onCancelarClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancelar reserva")
                }
            }
        }
    }
}