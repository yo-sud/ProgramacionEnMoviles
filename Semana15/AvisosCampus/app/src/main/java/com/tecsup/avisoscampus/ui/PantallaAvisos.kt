package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

// [G] Las tarjetas ya existen; conecta cada una en su paso
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: si no se mostró, avisar al ViewModel
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Tarjeta("Permiso de notificaciones") {
                // TODO Paso 2: lanzador, LaunchedEffect(tienePermiso), etiqueta de estado
                Text("Sin pedir")
                Button(onClick = { /* TODO Paso 2 */ }) { Text("Permitir notificaciones") }
            }
            Tarjeta("Avisos") {
                Button(onClick = { /* TODO Paso 3: vm.avisarAhora() */ }) { Text("Mostrar aviso") }
            }
            Tarjeta("Recordatorio") {
                // TODO Paso 5: faltan, recordarEn, cancelarRecordatorio
                Button(onClick = { /* TODO */ }) { Text("Recordar en 10 s") }
                OutlinedButton(onClick = { /* TODO */ }, enabled = false) { Text("Cancelar") }
            }
            Tarjeta("Receptor de anuncios") {
                // TODO Paso 6: DisposableEffect(escuchando) + registro/unregister
                Switch(checked = false, onCheckedChange = { /* TODO */ })
                Text("Escuchar el modo avión")
            }
            Tarjeta("Historial") {
                // TODO Paso 7: HistorialAvisos(avisos)
                Text("Sin avisos todavía")
            }
            // TODO Paso 8: recuadro de mensaje (error) + botón "Abrir ajustes"
        }
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            contenido()
        }
    }
}
