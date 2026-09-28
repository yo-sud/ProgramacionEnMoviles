package com.ochoa.tecsupfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ochoa.tecsupfit.model.Reserva

@Composable
fun PerfilScreen(
    reservas: List<Reserva>,
    modifier: Modifier = Modifier
) {
    val clasesCompletadas = reservas.count { it.estado == "Completada" }
    val clasesConfirmadas = reservas.count { it.estado == "Confirmada" }
    val rachaAsistencia = clasesCompletadas

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Alex Ochoa",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "alex.ochoa@tecsupfit.com",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EstadisticaItem(valor = clasesCompletadas.toString(), etiqueta = "Clases tomadas")
            EstadisticaItem(valor = clasesConfirmadas.toString(), etiqueta = "Próximas clases")
            EstadisticaItem(valor = rachaAsistencia.toString(), etiqueta = "Racha")
        }
    }
}

@Composable
private fun EstadisticaItem(valor: String, etiqueta: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = etiqueta, style = MaterialTheme.typography.bodySmall)
    }
}