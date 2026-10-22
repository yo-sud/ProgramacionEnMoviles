package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/** Chip en forma de píldora: activo relleno con el color del perfil, inactivo blanco con borde fino. */
@Composable
fun CafeChip(texto: String, seleccionado: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val c = MaterialTheme.colorScheme
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        modifier = modifier,
        label = { Text(texto, fontWeight = FontWeight.SemiBold) },
        shape = RoundedCornerShape(50),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = c.surfaceContainerLowest,
            labelColor = c.onSurface,
            selectedContainerColor = c.primary,
            selectedLabelColor = c.onPrimary
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = seleccionado,
            borderColor = c.outlineVariant,
            selectedBorderColor = Color.Transparent
        )
    )
}