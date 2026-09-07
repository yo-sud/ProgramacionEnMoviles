package com.ochoa.Tarea03

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun Tarea03Screen(modifier: Modifier = Modifier) {
    var nota1 by remember { mutableFloatStateOf(0f) } // Fundamentos de Programación (20%)[cite: 1]
    var nota2 by remember { mutableFloatStateOf(0f) } // Programación Orientada a Objetos (25%)[cite: 1]
    var nota3 by remember { mutableFloatStateOf(0f) } // Programación en Móviles (30%)[cite: 1]
    var nota4 by remember { mutableFloatStateOf(0f) } // Base de Datos (25%)[cite: 1]
}