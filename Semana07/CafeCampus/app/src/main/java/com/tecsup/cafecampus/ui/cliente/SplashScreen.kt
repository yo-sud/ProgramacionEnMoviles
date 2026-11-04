package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    mostrarAcciones: Boolean = false,
    onComenzar: () -> Unit = {},
    onTengoCuenta: () -> Unit = {},
    onTerminado: (() -> Unit)? = null
) {
    LaunchedEffect(Unit) {
        if (onTerminado != null) {
            delay(1200L)
            onTerminado()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("CafeCampus", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Cafetería TECSUP", style = MaterialTheme.typography.bodyMedium)
            if (mostrarAcciones) {
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = onComenzar, modifier = Modifier.fillMaxWidth()) {
                    Text("Comenzar")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onTengoCuenta, modifier = Modifier.fillMaxWidth()) {
                    Text("Ya tengo cuenta")
                }
            }
        }
    }
}
