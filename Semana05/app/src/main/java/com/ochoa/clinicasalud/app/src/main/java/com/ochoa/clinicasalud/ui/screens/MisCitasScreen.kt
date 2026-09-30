package com.ochoa.clinicasalud.ui.screens

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
import com.ochoa.clinicasalud.model.Cita

@Composable
fun MisCitasScreen(
    citas: List<Cita>,
    onCancelarCita: (Cita) -> Unit,
    modifier: Modifier = Modifier
) {
    var citaACancelar by remember { mutableStateOf<Cita?>(null) }

    if (citas.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Aún no tienes citas agendadas", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(citas) { cita ->
            TarjetaCita(
                cita = cita,
                onCancelarClick = { citaACancelar = cita }
            )
        }
    }

    citaACancelar?.let { cita ->
        AlertDialog(
            onDismissRequest = { citaACancelar = null },
            title = { Text("Cancelar cita") },
            text = {
                Text("¿Seguro que deseas cancelar tu cita con \"${cita.medico.nombre}\" el ${cita.fecha} a las ${cita.hora}?")
            },
            confirmButton = {
                TextButton(onClick = {
                    onCancelarCita(cita)
                    citaACancelar = null
                }) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { citaACancelar = null }) {
                    Text("Volver")
                }
            }
        )
    }
}

@Composable
private fun TarjetaCita(
    cita: Cita,
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
                        text = cita.medico.nombre,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${cita.fecha} - ${cita.hora}", style = MaterialTheme.typography.bodyMedium)
                }

                val colorEstado = if (cita.estado == "Confirmada") Color(0xFF2E7D32) else Color(0xFF757575)
                AssistChip(
                    onClick = { },
                    label = { Text(cita.estado) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = colorEstado.copy(alpha = 0.15f),
                        labelColor = colorEstado
                    )
                )
            }

            if (cita.estado == "Confirmada") {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = onCancelarClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancelar cita")
                }
            }
        }
    }
}