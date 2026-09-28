package com.ochoa.clinicasalud.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ochoa.clinicasalud.model.Medico
import com.ochoa.clinicasalud.model.medicosDeEjemplo

@Composable
fun InicioScreen(
    onMedicoClick: (Medico) -> Unit,
    modifier: Modifier = Modifier
) {
    val especialidades = medicosDeEjemplo.map { it.especialidad }.distinct()
    var especialidadSeleccionada by remember { mutableStateOf(especialidades.first()) }

    val medicosFiltrados = medicosDeEjemplo.filter { it.especialidad == especialidadSeleccionada }

    Column(modifier = modifier.fillMaxSize()) {

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(especialidades) { especialidad ->
                FilterChip(
                    selected = especialidad == especialidadSeleccionada,
                    onClick = { especialidadSeleccionada = especialidad },
                    label = { Text(especialidad) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(medicosFiltrados) { medico ->
                TarjetaMedico(medico = medico, onClick = { onMedicoClick(medico) })
            }
        }
    }
}

@Composable
fun TarjetaMedico(
    medico: Medico,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = medico.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = medico.especialidad, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Calificación: ${medico.calificacion}", style = MaterialTheme.typography.bodySmall)
        }
    }
}