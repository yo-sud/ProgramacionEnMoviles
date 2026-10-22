package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
                val vapor = Path().apply {
                    moveTo(36f, 38f); cubicTo(31f, 33f, 41f, 29f, 36f, 23f)
                    moveTo(50f, 38f); cubicTo(45f, 33f, 55f, 29f, 50f, 23f)
                }
                drawPath(vapor, linea, style = trazo(4.5f))

                val cuerpo = Path().apply {
                    moveTo(22f, 45f); lineTo(62f, 45f); lineTo(62f, 62f)
                    cubicTo(62f, 72f, 55f, 78f, 46f, 78f); lineTo(38f, 78f)
                    cubicTo(29f, 78f, 22f, 72f, 22f, 62f); close()
                }
                drawPath(cuerpo, linea, style = trazo(5f))

                val asa = Path().apply {
                    moveTo(62f, 50f); lineTo(68f, 50f)
                    cubicTo(77f, 50f, 77f, 64f, 68f, 64f); lineTo(62f, 64f)
                }
                drawPath(asa, linea, style = trazo(5f))

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
            drawRoundRect(blanco, Offset(14f, 76f), Size(64f, 8f), CornerRadius(4f, 4f))
            val asa = Path().apply {
                moveTo(68f, 44f); lineTo(72f, 44f)
                cubicTo(82f, 44f, 82f, 62f, 72f, 62f); lineTo(67f, 60f)
            }
            drawPath(asa, blanco, style = trazo(6f))
            val cuerpo = Path().apply {
                moveTo(24f, 38f); lineTo(68f, 38f); lineTo(68f, 56f)
                cubicTo(68f, 67f, 60f, 74f, 50f, 74f); lineTo(42f, 74f)
                cubicTo(32f, 74f, 24f, 67f, 24f, 56f); close()
            }
            drawPath(cuerpo, blanco)
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
            drawCircle(Color.White, radius = 38f, center = centro)
            drawCircle(Color(0xFFFED7AA), radius = 31f, center = centro, style = Stroke(width = 3f))
            drawCircle(Color(0xFFFFF7ED), radius = 29f, center = centro)
            drawCircle(Color(0xFFFACC15), radius = 9f, center = Offset(40f, 41f))
            drawCircle(Color(0xFF16A34A), radius = 12f, center = Offset(57f, 52f))
            drawCircle(Color(0xFFDC2626), radius = 6f, center = Offset(38f, 62f))
        }
    }
}

/** Vaso de cristal transparente con jugo de naranja y sorbete verde. */
@Composable
fun IlustracionJugo(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        enUnidades {
            val jugo = Path().apply {
                moveTo(31f, 42f); lineTo(69f, 42f); lineTo(65f, 80f)
                cubicTo(65f, 83f, 64f, 84f, 61f, 84f); lineTo(39f, 84f)
                cubicTo(36f, 84f, 35f, 83f, 35f, 80f); close()
            }
            drawPath(jugo, Color(0xFFF97316))
            drawLine(Color(0xFF22C55E), Offset(56f, 62f), Offset(65f, 14f), strokeWidth = 6f, cap = StrokeCap.Round)
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

/** Sándwich visto de lado: pan, lechuga, tomate, queso y pan. */
@Composable
fun IlustracionSandwich(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        enUnidades {
            // Pan de abajo, queso y tomate
            drawRoundRect(Color(0xFFC98A3B), Offset(16f, 66f), Size(68f, 14f), CornerRadius(7f, 7f))
            drawRoundRect(Color(0xFFFACC15), Offset(14f, 58f), Size(72f, 9f), CornerRadius(3f, 3f))
            drawRoundRect(Color(0xFFEF4444), Offset(18f, 52f), Size(64f, 8f), CornerRadius(4f, 4f))
            // Lechuga ondulada
            val lechuga = Path().apply {
                moveTo(12f, 46f); lineTo(12f, 48f)
                cubicTo(16f, 58f, 24f, 58f, 28f, 48f)
                cubicTo(32f, 58f, 40f, 58f, 44f, 48f)
                cubicTo(48f, 58f, 56f, 58f, 60f, 48f)
                cubicTo(64f, 58f, 72f, 58f, 76f, 48f)
                cubicTo(80f, 58f, 86f, 56f, 88f, 48f)
                lineTo(88f, 46f); close()
            }
            drawPath(lechuga, Color(0xFF65A30D))
            // Pan de arriba con semillas
            val pan = Path().apply {
                moveTo(14f, 48f); cubicTo(14f, 22f, 86f, 22f, 86f, 48f); close()
            }
            drawPath(pan, Color(0xFFE0A458))
            val semilla = Color(0xFFFFF1C9)
            drawOval(semilla, Offset(34f, 31f), Size(7f, 4f))
            drawOval(semilla, Offset(47f, 28f), Size(7f, 4f))
            drawOval(semilla, Offset(60f, 31f), Size(7f, 4f))
            drawOval(semilla, Offset(41f, 38f), Size(7f, 4f))
            drawOval(semilla, Offset(54f, 38f), Size(7f, 4f))
        }
    }
}

/** Empanada: media luna dorada con el borde repulgado. */
@Composable
fun IlustracionEmpanada(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        enUnidades {
            drawOval(Color.Black.copy(alpha = 0.08f), Offset(14f, 66f), Size(72f, 10f))
            val cuerpo = Path().apply {
                moveTo(12f, 66f); cubicTo(12f, 26f, 88f, 26f, 88f, 66f); close()
            }
            drawPath(cuerpo, Color(0xFFF0B45C))
            val brillo = Path().apply {
                moveTo(24f, 56f); cubicTo(28f, 40f, 50f, 34f, 64f, 40f)
            }
            drawPath(brillo, Color.White.copy(alpha = 0.45f), style = trazo(4f))
            val borde = Color(0xFFD08A2E)
            drawLine(borde, Offset(12f, 66f), Offset(88f, 66f), strokeWidth = 2.5f)
            var x = 16f
            while (x <= 84f) {
                drawCircle(borde, radius = 3.6f, center = Offset(x, 66f))
                x += 7.5f
            }
        }
    }
}

/** Jarra de cristal con chicha morada y hielo. */
@Composable
fun IlustracionChicha(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        enUnidades {
            val cristal = Color.White.copy(alpha = 0.9f)
            val asa = Path().apply {
                moveTo(70f, 40f); cubicTo(88f, 40f, 88f, 68f, 70f, 68f)
            }
            drawPath(asa, cristal, style = trazo(5f))

            val jarra = Path().apply {
                moveTo(26f, 24f); lineTo(72f, 24f); lineTo(70f, 80f)
                cubicTo(70f, 85f, 67f, 87f, 63f, 87f); lineTo(35f, 87f)
                cubicTo(31f, 87f, 28f, 85f, 28f, 80f); close()
            }
            drawPath(jarra, Color.White.copy(alpha = 0.3f))

            val liquido = Path().apply {
                moveTo(27.4f, 38f); lineTo(70.6f, 38f); lineTo(70f, 80f)
                cubicTo(70f, 85f, 67f, 87f, 63f, 87f); lineTo(35f, 87f)
                cubicTo(31f, 87f, 28f, 85f, 28f, 80f); close()
            }
            drawPath(liquido, Color(0xFF7E22CE))

            drawRoundRect(Color.White.copy(alpha = 0.45f), Offset(36f, 41f), Size(12f, 12f), CornerRadius(3f, 3f))
            drawRoundRect(Color.White.copy(alpha = 0.45f), Offset(52f, 46f), Size(11f, 11f), CornerRadius(3f, 3f))
            drawLine(Color.White.copy(alpha = 0.35f), Offset(33f, 58f), Offset(33f, 78f), strokeWidth = 3f, cap = StrokeCap.Round)

            drawPath(jarra, cristal, style = trazo(3.5f))
        }
    }
}

/** Elige la ilustración según la clave de Producto.icono. */
@Composable
fun IlustracionProducto(clave: String, modifier: Modifier = Modifier) {
    when (clave) {
        "cafe" -> IlustracionTaza(modifier)
        "plato" -> IlustracionSopa(modifier)
        "jugo" -> IlustracionJugo(modifier)
        "pan" -> IlustracionSandwich(modifier)
        "botella" -> IlustracionChicha(modifier)
        "dulce" -> IlustracionEmpanada(modifier)
        else -> IlustracionTaza(modifier)
    }
}

private fun fondoDe(clave: String): Color = when (clave) {
    "cafe" -> Color(0xFFE6D3C1)
    "plato" -> Color(0xFFFFEDD5)
    "jugo" -> Color(0xFFFFE6CF)
    "pan" -> Color(0xFFFEF3C7)
    "botella" -> Color(0xFFEDE3F8)
    "dulce" -> Color(0xFFFCE7C6)
    else -> Color(0xFFFFEDD5)
}

/** Imagen del producto: ilustración centrada sobre su color de fondo. El tamaño lo define el `modifier`. */
@Composable
fun ProductoImagen(icono: String, modifier: Modifier = Modifier) {
    Box(modifier.background(fondoDe(icono)), contentAlignment = Alignment.Center) {
        IlustracionProducto(icono, Modifier.fillMaxHeight(0.78f).aspectRatio(1f))
    }
}