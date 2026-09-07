package com.ochoa.Tarea03

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun Tarea03(modifier: Modifier = Modifier) {
    var nota1 by remember { mutableFloatStateOf(0f) }
    var nota2 by remember { mutableFloatStateOf(0f) }
    var nota3 by remember { mutableFloatStateOf(0f) }
    var nota4 by remember { mutableFloatStateOf(0f) }

    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var calculado by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Calculadora de Notas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Ajusta las notas con los deslizadores",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(16.dp))

        CursoItem(
            nombre = "Evaluación 1",
            porcentaje = "(25%)",
            nota = nota1,
            onNotaChange = { nota1 = it; calculado = false }
        )
        CursoItem(
            nombre = "Evaluación 2",
            porcentaje = "(25%)",
            nota = nota2,
            onNotaChange = { nota2 = it; calculado = false }
        )
        CursoItem(
            nombre = "Evaluación 3",
            porcentaje = "(25%)",
            nota = nota3,
            onNotaChange = { nota3 = it; calculado = false }
        )
        CursoItem(
            nombre = "Evaluación 4",
            porcentaje = "(25%)",
            nota = nota4,
            onNotaChange = { nota4 = it; calculado = false }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Control Switch para redondear
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Redondear promedio final")
            Switch(
                checked = redondear,
                onCheckedChange = { redondear = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Control Checkbox para confirmar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = confirmado,
                onCheckedChange = { confirmado = it }
            )
            Text(text = "Confirmar que los datos son correctos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { calculado = true },
            enabled = confirmado,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CALCULAR PROMEDIO")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (calculado) {
            val promedioSimple = (nota1 + nota2 + nota3 + nota4) / 4f
            val promedioFinal = if (redondear) promedioSimple.roundToInt().toFloat() else promedioSimple
            val esAprobado = promedioFinal >= 10.5f

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (esAprobado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Promedio Final:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = if (redondear) "${promedioFinal.toInt()}" else String.format("%.2f", promedioFinal),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (esAprobado) "✓ APROBADO" else "✗ DESAPROBADO",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (esAprobado) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun CursoItem(
    nombre: String,
    porcentaje: String,
    nota: Float,
    onNotaChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$nombre $porcentaje")
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = "${nota.toInt()}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Slider(
            value = nota,
            onValueChange = onNotaChange,
            valueRange = 0f..20f,
            steps = 19
        )
    }
}