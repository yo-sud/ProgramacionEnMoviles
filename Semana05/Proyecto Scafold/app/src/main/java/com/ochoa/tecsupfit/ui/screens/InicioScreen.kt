package com.ochoa.tecsupfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ochoa.tecsupfit.model.ClaseGimnasio
import com.ochoa.tecsupfit.model.clasesDeEjemplo
import androidx.compose.foundation.clickable

@Composable
fun InicioScreen(
    onClaseClick: (ClaseGimnasio) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(clasesDeEjemplo) { clase ->
            TarjetaClase(clase = clase, onClick = { onClaseClick(clase) })
        }
    }
}

@Composable
fun TarjetaClase(
    clase: ClaseGimnasio,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = clase.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = clase.horario, style = MaterialTheme.typography.bodyMedium)
        }
    }
}