package com.tecsup.cafecampus.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Selector tipo píldora: la opción activa se muestra blanca, con el texto del color del perfil. */
@Composable
fun SelectorSegmentado(
    opciones: List<String>,
    seleccion: Int,
    onSeleccion: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(c.onSurface.copy(alpha = 0.07f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        opciones.forEachIndexed { i, texto ->
            val activa = i == seleccion
            val fondo by animateColorAsState(
                if (activa) c.surfaceContainerLowest else Color.Transparent,
                label = "fondoSegmento"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(forma)
                    .background(fondo)
                    .then(if (activa) Modifier.border(1.dp, c.outlineVariant, forma) else Modifier)
                    .selectable(selected = activa, onClick = { onSeleccion(i) }, role = Role.Tab),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    texto,
                    fontWeight = FontWeight.SemiBold,
                    color = if (activa) c.primary else c.onSurfaceVariant
                )
            }
        }
    }
}