package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Claves válidas de Producto.icono. */
val ICONOS_PRODUCTO = listOf("cafe", "plato", "jugo", "pan", "botella", "dulce")

/** Ícono simple de Material (lo usa el selector del formulario). */
fun iconoDe(clave: String): ImageVector = when (clave) {
    "cafe" -> Icons.Filled.LocalCafe
    "plato" -> Icons.Filled.Restaurant
    "jugo" -> Icons.Filled.LocalDrink
    "pan" -> Icons.Filled.LunchDining
    "botella" -> Icons.Filled.WaterDrop
    "dulce" -> Icons.Filled.Cookie
    else -> Icons.Filled.Fastfood
}

/** Miniatura cuadrada con la ilustración del producto (detalle, listas del administrador, etc.). */
@Composable
fun ProductoIcono(icono: String, modifier: Modifier = Modifier, tamano: Dp = 56.dp) {
    ProductoImagen(icono, modifier.size(tamano).clip(RoundedCornerShape(12.dp)))
}