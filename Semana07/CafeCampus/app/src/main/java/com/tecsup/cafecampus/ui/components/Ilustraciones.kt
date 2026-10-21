package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Dibuja en una cuadrícula de 100 x 100 unidades, sin importar el tamaño real del Canvas. */
private fun DrawScope.enUnidades(bloque: DrawScope.() -> Unit) {
    scale(scaleX = size.width / 100f, scaleY = size.height / 100f, pivot = Offset.Zero, block = bloque)
}

private fun trazo(ancho: Float) = Stroke(width = ancho, cap = StrokeCap.Round, join = StrokeJoin.Round)

/** Logo de la app: taza amplia en contorno con dos líneas de vapor, sobre un cuadrado redondeado. */
@Composable
fun LogoCafe(modifier: Modifier = Modifier, tamano: Dp = 88.dp) {
    val fondo = MaterialTheme.colorScheme.primary
    val linea = MaterialTheme.colorScheme.onPrimary
    Box(
        modifier = modifier.size(tamano).clip(RoundedCornerShape(tamano * 0.27f)).background(fondo),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.size(tamano * 0.64f)) {
            enUnidades {
                // Vapor (2 líneas onduladas)
                val vapor = Path().apply {
                    moveTo(36f, 38f); cubicTo(31f, 33f, 41f, 29f, 36f, 23f)
                    moveTo(50f, 38f); cubicTo(45f, 33f, 55f, 29f, 50f, 23f)
                }
                drawPath(vapor, linea, style = trazo(4.5f))

                // Cuerpo de la taza (solo contorno)
                val cuerpo = Path().apply {
                    moveTo(22f, 45f); lineTo(62f, 45f); lineTo(62f, 62f)
                    cubicTo(62f, 72f, 55f, 78f, 46f, 78f); lineTo(38f, 78f)
                    cubicTo(29f, 78f, 22f, 72f, 22f, 62f); close()
                }
                drawPath(cuerpo, linea, style = trazo(5f))

                // Asa
                val asa = Path().apply {
                    moveTo(62f, 50f); lineTo(68f, 50f)
                    cubicTo(77f, 50f, 77f, 64f, 68f, 64f); lineTo(62f, 64f)
                }
                drawPath(asa, linea, style = trazo(5f))

                // Plato
                drawLine(linea, Offset(28f, 85f), Offset(56f, 85f), strokeWidth = 5f, cap = StrokeCap.Round)
            }
        }
    }
}

/** Taza blanca con plato y café marrón. */
@Composable
fun IlustracionTaza(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        enUnidades {
            val blanco = Color.White
            // Plato
            drawRoundRect(blanco, Offset(14f, 76f), Size(64f, 8f), CornerRadius(4f, 4f))
            // Asa
            val asa = Path().apply {
                moveTo(68f, 44f); lineTo(72f, 44f)
                cubicTo(82f, 44f, 82f, 62f, 72f, 62f); lineTo(67f, 60f)
            }
            drawPath(asa, blanco, style = trazo(6f))
            // Cuerpo
            val cuerpo = Path().apply {
                moveTo(24f, 38f); lineTo(68f, 38f); lineTo(68f, 56f)
                cubicTo(68f, 67f, 60f, 74f, 50f, 74f); lineTo(42f, 74f)
                cubicTo(32f, 74f, 24f, 67f, 24f, 56f); close()
            }
            drawPath(cuerpo, blanco)
            // Café
            drawOval(Color(0xFF5D4037), topLeft = Offset(26f, 35f), size = Size(40f, 10f))
        }
    }
}

/** Plato de sopa visto desde arriba, con 3 bolitas: verde (grande), amarilla (mediana) y roja (pequeña). */
@Composable
fun IlustracionSopa(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        enUnidades {
            val centro = Offset(50f, 50f)
            drawCircle(Color.White, radius = 42f, center = centro)
            drawCircle(Color(0xFFFAD3A4), radius = 29f, center = centro)
            drawCircle(Color(0xFFFACC15), radius = 9f, center = Offset(40f, 41f))   // amarilla (mediana)
            drawCircle(Color(0xFF16A34A), radius = 12f, center = Offset(57f, 52f))  // verde (grande)
            drawCircle(Color(0xFFDC2626), radius = 6f, center = Offset(38f, 62f))   // roja (pequeña)
        }
    }
}

/** Vaso de cristal transparente con jugo de naranja y sorbete verde. */
@Composable
fun IlustracionJugo(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        enUnidades {
            // Jugo
            val jugo = Path().apply {
                moveTo(31f, 42f); lineTo(69f, 42f); lineTo(65f, 80f)
                cubicTo(65f, 83f, 64f, 84f, 61f, 84f); lineTo(39f, 84f)
                cubicTo(36f, 84f, 35f, 83f, 35f, 80f); close()
            }
            drawPath(jugo, Color(0xFFF97316))
            // Sorbete (verde, inclinado)
            drawLine(Color(0xFF22C55E), Offset(56f, 62f), Offset(65f, 14f), strokeWidth = 6f, cap = StrokeCap.Round)
            // Vaso (cristal)
            val vaso = Path().apply {
                moveTo(28f, 28f); lineTo(72f, 28f); lineTo(66f, 82f)
                cubicTo(66f, 86f, 64f, 88f, 61f, 88f); lineTo(39f, 88f)
                cubicTo(36f, 88f, 34f, 86f, 34f, 82f); close()
            }
            drawPath(vaso, Color.White.copy(alpha = 0.28f))
            drawPath(vaso, Color.White.copy(alpha = 0.9f), style = trazo(3.5f))
        }
    }
}