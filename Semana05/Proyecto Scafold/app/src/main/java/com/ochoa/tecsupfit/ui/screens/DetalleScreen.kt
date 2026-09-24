package com.ochoa.tecsupfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ochoa.tecsupfit.model.ClaseGimnasio

@Composable
fun DetalleScreen(
    clase: ClaseGimnasio,
    onReservarClick: (horarioElegido: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val horariosDisponibles = listOf(clase.horario, "12:00 PM", "4:00 PM")
    var horarioSeleccionado by remember { mutableStateOf(horariosDisponibles.first()) }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = clase.nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        InfoFila(etiqueta = "Instructor", valor = clase.instructor)
        InfoFila(etiqueta = "Cupos disponibles", valor = clase.cupoDisponible.toString())
        InfoFila(etiqueta = "Categoría", valor = clase.categoria)

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Elige un horario",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(horariosDisponibles) { horario ->
                FilterChip(
                    selected = horario == horarioSeleccionado,
                    onClick = { horarioSeleccionado = horario },
                    label = { Text(horario) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onReservarClick(horarioSeleccionado) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reservar cupo")
        }
    }
}

@Composable
private fun InfoFila(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        Text(text = valor, style = MaterialTheme.typography.bodyMedium)
    }
}