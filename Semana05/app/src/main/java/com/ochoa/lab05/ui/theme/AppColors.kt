package com.ochoa.lab05.ui.theme

import androidx.compose.ui.graphics.Color
import kotlin.math.absoluteValue

object AppColors {

    val Background = Color(0xFFF3E8FF)
    val CardBackground = Color(0xFFFAFAFA)
    val CardBorder = Color(0xFFCCCCCC)

    val Primary = Color(0xFF6750A4)
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryLight = Color(0xFFEADDFF)
    val IconContainer = Color(0xFFEADDFF)

    val LogoutBackground = Color(0xFFFF9800)
    val LogoutText = Color(0xFFD32F2F)

    val ProfileGradientStart = Color(0xFF3B1D6E)
    val ProfileGradientEnd = Color(0xFF7B1F3A)
    val CoverColors = listOf(
        Color(0xFF6750A4),
        Color(0xFF8E4585),
        Color(0xFF4F6FA8),
        Color(0xFF3F8F8B),
        Color(0xFFB0605A)
    )

    object AvatarPalette {
        private val colors = CoverColors

        operator fun get(key: Any?): Color =
            colors[(key?.hashCode() ?: 0).absoluteValue % colors.size]
    }
}