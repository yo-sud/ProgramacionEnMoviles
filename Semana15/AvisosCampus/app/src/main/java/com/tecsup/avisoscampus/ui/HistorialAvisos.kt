package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.Aviso

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    if (avisos.isEmpty()) {
        Text("Sin avisos todavía")
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        avisos.forEach { aviso ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(aviso.titulo, style = MaterialTheme.typography.titleSmall)
                    Text(aviso.texto, style = MaterialTheme.typography.bodyMedium)
                    Text("${aviso.origen.texto} · ${aviso.hora}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
