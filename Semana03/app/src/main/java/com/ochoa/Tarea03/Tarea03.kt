package com.ochoa.Tarea03

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun Tarea03(modifier: Modifier = Modifier) {
    var nota1 by remember { mutableFloatStateOf(0f) }
    var nota2 by remember { mutableFloatStateOf(0f) }
    var nota3 by remember { mutableFloatStateOf(0f) }
    var nota4 by remember { mutableFloatStateOf(0f) }

    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var calculado by remember { mutableStateOf(false) }

    val fondoDegradado = Brush.verticalGradient(
        colors = listOf(Color(0xFFD8CCEF), Color.White)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondoDegradado)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6750A4))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Registro de Notas",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Notas del ciclo",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "Desliza para asignar cada nota (0 a 20)",
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            CursoItem("Fundamentos de Programación", "(20%)", nota1) { nota1 = it; calculado = false }
            CursoItem("Programación Orientada a Objetos", "(25%)", nota2) { nota2 = it; calculado = false }
            CursoItem("Programación en Móviles", "(30%)", nota3) { nota3 = it; calculado = false }
            CursoItem("Base de Datos", "(25%)", nota4) { nota4 = it; calculado = false }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Redondear promedio final")
                Switch(
                    checked = redondear,
                    onCheckedChange = { redondear = it; calculado = false },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF6750A4)
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = confirmado,
                    onCheckedChange = { confirmado = it; calculado = false },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF6750A4)
                    )
                )
                Text("Confirmo que las notas son correctas")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { calculado = true },
                enabled = confirmado,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6750A4),
                    disabledContainerColor = Color(0xFFCABADD)
                )
            ) {
                Text(
                    text = "CALCULAR PROMEDIO",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (calculado) {
                val promedioPonderado = (nota1 * 0.20f) + (nota2 * 0.25f) + (nota3 * 0.30f) + (nota4 * 0.25f)
                val promedioFinal = if (redondear) promedioPonderado.roundToInt().toFloat() else promedioPonderado
                val esAprobado = promedioFinal >= 10.5f

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Promedio ponderado: ${String.format("%.2f", promedioPonderado)}",
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Promedio final: ",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6750A4),
                                fontSize = 18.sp
                            )
                            Text(
                                text = if (redondear) "${promedioFinal.toInt()}" else String.format("%.2f", promedioFinal),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6750A4),
                                fontSize = 18.sp
                            )
                        }
                        if (redondear) {
                            Text("(redondeado)", color = Color.Gray, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = if (esAprobado) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = if (esAprobado) "APROBADO" else "DESAPROBADO",
                                color = if (esAprobado) Color(0xFF2E7D32) else Color(0xFFC62828),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✓ Promedio calculado correctamente",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Asigna las notas y confirma para calcular",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Desarrollado por: Yamil",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CursoItem(
    nombre: String,
    porcentaje: String,
    nota: Float,
    onNotaChange: (Float) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(text = nombre, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(text = " $porcentaje", color = Color(0xFF6750A4), fontSize = 15.sp)
            }
            Surface(
                color = Color(0xFFE8DEF8),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${nota.roundToInt()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF6750A4),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
        Slider(
            value = nota,

            onValueChange = { onNotaChange(it.roundToInt().toFloat()) },
            valueRange = 0f..20f,
            steps = 19,
            interactionSource = interactionSource,
            thumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(color = Color(0xFF6750A4), shape = CircleShape)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(4.dp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color(0xFF6750A4),
                        inactiveTrackColor = Color(0xFFE0D6F0),
                        activeTickColor = Color(0xFF6750A4),
                        inactiveTickColor = Color(0xFFE0D6F0)
                    ),
                    drawStopIndicator = null,
                    thumbTrackGapSize = 0.dp,
                    trackInsideCornerSize = 0.dp
                )
            }
        )
    }
}