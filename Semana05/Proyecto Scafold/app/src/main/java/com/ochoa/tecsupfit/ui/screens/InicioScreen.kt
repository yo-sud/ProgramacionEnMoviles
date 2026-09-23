package com.ochoa.tecsupfit.ui.screens

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
import com.ochoa.tecsupfit.model.ClaseGimnasio
import com.ochoa.tecsupfit.model.clasesDeEjemplo

@Composable
fun InicioScreen(
    onClaseClick: (ClaseGimnasio) -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado del filtro seleccionado (selección única), manejado con remember
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }
    val categorias = listOf("Hoy", "Esta semana")

    // Filtra la lista según la categoría elegida
    val clasesFiltradas = clasesDeEjemplo.filter { it.categoria == filtroSeleccionado }

    Column(modifier = modifier.fillMaxSize()) {

        // LazyRow de chips de filtro
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { categoria ->
                FilterChip(
                    selected = categoria == filtroSeleccionado,
                    onClick = { filtroSeleccionado = categoria },
                    label = { Text(categoria) }
                )
            }
        }

        // LazyColumn de clases filtradas
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(clasesFiltradas) { clase ->
                TarjetaClase(clase = clase, onClick = { onClaseClick(clase) })
            }
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