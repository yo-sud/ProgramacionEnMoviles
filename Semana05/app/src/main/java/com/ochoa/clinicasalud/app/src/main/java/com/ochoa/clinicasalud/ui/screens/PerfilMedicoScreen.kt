package com.ochoa.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ochoa.clinicasalud.model.Medico

@Composable
fun PerfilMedicoScreen(
    medico: Medico,
    onAgendarCitaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = medico.nombre,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = medico.especialidad,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        InfoFila(etiqueta = "Calificación", valor = medico.calificacion.toString())

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Acerca del médico",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = medico.descripcion,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onAgendarCitaClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agendar cita")
        }
    }
}

@Composable
private fun InfoFila(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        Text(text = valor, style = MaterialTheme.typography.bodyMedium)
    }
}