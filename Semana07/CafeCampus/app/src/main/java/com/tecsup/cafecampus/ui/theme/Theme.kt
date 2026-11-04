package com.tecsup.cafecampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.tecsup.cafecampus.model.Rol

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    secondary = SecondaryLight,
    tertiary = TertiaryLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    secondary = SecondaryLight,
    tertiary = TertiaryLight
)

@Composable
fun CafeTheme(
    rol: Rol = Rol.CLIENTE,
    oscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (oscuro) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
