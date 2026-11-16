package com.ochoa.campusgps.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = Color(0xFF1A6BA8),
    onPrimary = Color.White,
    error = Color(0xFFB3261E)
)

@Composable
fun CampusGPSTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaClaro, content = content)
}
