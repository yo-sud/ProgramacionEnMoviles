package com.ochoa.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ochoa.clinicasalud.model.Medico

@Composable
fun AgendarCitaScreen(
    medico: Medico,
    onConfirmarClick: (fecha: String, hora: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val fechasDisponibles = listOf("Lun 05 Oct", "Mar 06 Oct", "Mie 07 Oct")
    val horasDisponibles = listOf("9:00 AM", "11:00 AM", "3:00 PM")

    var fechaSeleccionada by remember { mutableStateOf(fechasDisponibles.first()) }
    var horaSeleccionada by remember { mutableStateOf(horasDisponibles.first()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Agendar cita con ${medico.nombre}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Elige una fecha", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(fechasDisponibles) { fecha ->
                FilterChip(
                    selected = fecha == fechaSeleccionada,
                    onClick = { fechaSeleccionada = fecha },
                    label = { Text(fecha) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Elige una hora", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(horasDisponibles) { hora ->
                FilterChip(
                    selected = hora == horaSeleccionada,
                    onClick = { horaSeleccionada = hora },
                    label = { Text(hora) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onConfirmarClick(fechaSeleccionada, horaSeleccionada) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar cita")
        }
    }
}