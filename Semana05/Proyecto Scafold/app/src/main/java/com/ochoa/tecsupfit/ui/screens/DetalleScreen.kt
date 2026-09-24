package com.ochoa.tecsupfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ochoa.tecsupfit.model.ClaseGimnasio

@Composable
fun DetalleScreen(
    clase: ClaseGimnasio,
    onReservarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = clase.nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        InfoFila(etiqueta = "Instructor", valor = clase.instructor)
        InfoFila(etiqueta = "Horario", valor = clase.horario)
        InfoFila(etiqueta = "Cupos disponibles", valor = clase.cupoDisponible.toString())
        InfoFila(etiqueta = "Categoría", valor = clase.categoria)

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = onReservarClick, modifier = Modifier.fillMaxWidth()) {
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