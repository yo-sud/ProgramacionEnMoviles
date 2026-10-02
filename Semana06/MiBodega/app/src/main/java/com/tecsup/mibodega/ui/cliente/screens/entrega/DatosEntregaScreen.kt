package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.componentes.CampoTexto
import androidx.compose.material3.MaterialTheme

@Composable
fun DatosEntregaScreen(
    carrito: List<ItemCarrito>,
    onVolver: () -> Unit,
    onConfirmarPedido: () -> Unit
) {
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text(text = "Datos de entrega", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(Modifier.height(16.dp))
        CampoTexto(
            etiqueta = "Dirección de entrega",
            valor = direccion,
            onValorCambia = { direccion = it },
            placeholder = "Ingresa tu direccion de domicilio"
        )
        Spacer(modifier = Modifier.height(16.dp))
        CampoTexto(
            etiqueta = "Referencia",
            valor = referencia,
            onValorCambia = { referencia = it },
            placeholder = "Ingresa una referencia de la zona"
        )
        Spacer(modifier = Modifier.height(16.dp))
        CampoTexto(
            etiqueta = "Teléfono de contacto",
            valor = telefono,
            onValorCambia = { telefono = it },
            placeholder = "Ingresa tu teléfono",
            teclado = KeyboardType.Phone
        )
    }
}
