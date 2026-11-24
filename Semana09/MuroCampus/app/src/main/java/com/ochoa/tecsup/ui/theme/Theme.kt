package com.ochoa.tecsup.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val EsquemaClaro = lightColorScheme(
    primary = Azul, onPrimary = Color.White,
    primaryContainer = AzulContenedor, onPrimaryContainer = Color(0xFF0B2A5B),
    background = FondoClaro, surface = FondoClaro,
    surfaceContainerLowest = Color.White
)

private val EsquemaOscuro = darkColorScheme(
    primary = AzulSobreOscuro, onPrimary = Color(0xFF0B2A5B),
    primaryContainer = Color(0xFF1E3A6D), onPrimaryContainer = AzulContenedor,
    background = FondoOscuro, surface = FondoOscuro,
    surfaceContainerLowest = Color(0xFF181D27)
)

private val Formas = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

/** Tema de MuroCampus: azul, con modo oscuro automático según el sistema. */
@Composable
fun MuroCampusTheme(oscuro: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val esquema = if (oscuro) EsquemaOscuro else EsquemaClaro
    val vista = LocalView.current
    if (!vista.isInEditMode) {
        SideEffect {
            val ventana = (vista.context as? Activity)?.window ?: return@SideEffect
            ventana.statusBarColor = esquema.primary.toArgb()
            ventana.navigationBarColor = esquema.background.toArgb()
            WindowCompat.getInsetsController(ventana, vista).apply {
                isAppearanceLightStatusBars = oscuro      // barra clara -> íconos oscuros
                isAppearanceLightNavigationBars = !oscuro
            }
        }
    }
    MaterialTheme(colorScheme = esquema, typography = Tipografia, shapes = Formas, content = content)
}
