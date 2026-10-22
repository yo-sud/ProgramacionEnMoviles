package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Tarjeta base: 12 dp de radio, borde fino, fondo blanco. `relleno = 0` permite imágenes a sangre. */
@Composable
fun CafeCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    colorFondo: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    espaciado: Int = 6,
    relleno: Int = 16,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(12.dp)
    val clicable = if (onClick != null) Modifier.clip(forma).clickable(onClick = onClick) else Modifier
    Card(
        modifier = modifier.fillMaxWidth().then(clicable),
        shape = forma,
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(Modifier.padding(relleno.dp), verticalArrangement = Arrangement.spacedBy(espaciado.dp), content = contenido)
    }
}