package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Botón de 10 dp de radio. `secundario = true` lo dibuja con borde del color del perfil. */
@Composable
fun CafeButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    secundario: Boolean = false
) {
    val forma = RoundedCornerShape(10.dp)
    val medida = modifier.fillMaxWidth().height(48.dp)
    if (secundario) {
        val colorBorde = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        OutlinedButton(
            onClick = onClick,
            modifier = medida,
            enabled = enabled,
            shape = forma,
            border = BorderStroke(1.5.dp, colorBorde)
        ) { Text(texto, fontWeight = FontWeight.SemiBold) }
    } else {
        Button(onClick = onClick, modifier = medida, enabled = enabled, shape = forma) {
            Text(texto, fontWeight = FontWeight.SemiBold)
        }
    }
}