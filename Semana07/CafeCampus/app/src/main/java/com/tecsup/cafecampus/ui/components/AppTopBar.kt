package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

/** Barra superior. `centrado = true` pone el título al centro con un estilo más compacto. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titulo: String,
    onVolver: (() -> Unit)? = null,
    centrado: Boolean = false,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    val titleContent: @Composable () -> Unit = {
        Text(
            titulo,
            style = if (centrado) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
    val navegacion: @Composable () -> Unit = {
        if (onVolver != null) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        }
    }
    val colores = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)

    if (centrado) {
        CenterAlignedTopAppBar(title = titleContent, navigationIcon = navegacion, actions = acciones, colors = colores)
    } else {
        TopAppBar(title = titleContent, navigationIcon = navegacion, actions = acciones, colors = colores)
    }
}