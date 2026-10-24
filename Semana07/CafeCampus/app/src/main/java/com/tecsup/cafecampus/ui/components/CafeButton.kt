package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.ui.theme.EstadoCancelado

/**
 * Botón de 10 dp de radio. `secundario = true` lo dibuja con borde del color del perfil.
 * `peligro = true` lo pinta en rojo (acciones destructivas como cerrar sesión).
 */
@Composable
fun CafeButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    secundario: Boolean = false,
    peligro: Boolean = false
) {
    val forma = RoundedCornerShape(10.dp)
    val medida = modifier.fillMaxWidth().height(48.dp)
    if (secundario) {
        val acento = if (peligro) EstadoCancelado else MaterialTheme.colorScheme.primary
        val colorBorde = if (enabled) acento else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        OutlinedButton(
            onClick = onClick,
            modifier = medida,
            enabled = enabled,
            shape = forma,
            border = BorderStroke(1.5.dp, colorBorde),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = acento)
        ) { Text(texto, fontWeight = FontWeight.SemiBold) }
    } else {
        val colores = if (peligro) {
            ButtonDefaults.buttonColors(containerColor = EstadoCancelado, contentColor = Color.White)
        } else {
            ButtonDefaults.buttonColors()
        }
        Button(onClick = onClick, modifier = medida, enabled = enabled, shape = forma, colors = colores) {
            Text(texto, fontWeight = FontWeight.SemiBold)
        }
    }
}