package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.theme.EstadoListo

private data class Paso(val titulo: String, val detalle: String)

private val PASOS = listOf(
    Paso("Recibido", "Tu pedido llegó a la cocina"),
    Paso("Preparando", "Están cocinando tu pedido"),
    Paso("Listo", "Puedes pasar a recogerlo"),
    Paso("Entregado", "¡Buen provecho!")
)

/**
 * Línea de tiempo vertical: pasos completados en verde, el paso actual con círculo y halo del
 * color del perfil, y los pendientes en gris. Con ENTREGADO todos los pasos quedan completados.
 */
@Composable
fun StepperVertical(estado: EstadoPedido, modifier: Modifier = Modifier) {
    val colores = MaterialTheme.colorScheme
    val actual = when (estado) {
        EstadoPedido.RECIBIDO -> 0
        EstadoPedido.PREPARANDO -> 1
        EstadoPedido.LISTO -> 2
        EstadoPedido.ENTREGADO -> PASOS.size
        EstadoPedido.CANCELADO -> -1
    }

    Column(modifier.fillMaxWidth()) {
        PASOS.forEachIndexed { i, paso ->
            val esUltimo = i == PASOS.lastIndex
            val hecho = i < actual
            val enCurso = i == actual

            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                Column(
                    modifier = Modifier.width(36.dp).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Indicador(hecho = hecho, enCurso = enCurso)
                    if (!esUltimo) {
                        Box(
                            Modifier
                                .width(3.dp)
                                .weight(1f)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (hecho) colores.primary else colores.outlineVariant)
                        )
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column(
                    Modifier
                        .padding(top = 6.dp, bottom = if (esUltimo) 0.dp else 28.dp)
                        .alpha(if (hecho || enCurso) 1f else 0.5f)
                ) {
                    Text(paso.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(paso.detalle, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun Indicador(hecho: Boolean, enCurso: Boolean) {
    val colores = MaterialTheme.colorScheme
    Box(Modifier.size(36.dp), contentAlignment = Alignment.Center) {
        when {
            hecho -> Box(
                Modifier.size(28.dp).clip(CircleShape).background(EstadoListo),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Check, contentDescription = "Completado", tint = Color.White, modifier = Modifier.size(16.dp))
            }
            enCurso -> {
                Box(Modifier.size(36.dp).clip(CircleShape).background(colores.primary.copy(alpha = 0.18f)))
                Box(Modifier.size(22.dp).clip(CircleShape).background(colores.primary))
            }
            else -> Box(Modifier.size(28.dp).clip(CircleShape).border(2.dp, colores.outlineVariant, CircleShape))
        }
    }
}