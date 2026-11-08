package com.tecsup.pokemon.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(vm: LoginViewModel, onLogin: () -> Unit) {
    val sesion by vm.sesion.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(sesion) { if (sesion != null) onLogin() }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pokédex", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(correo, { correo = it }, label = { Text("Correo") }, singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
        OutlinedTextField(clave, { clave = it }, label = { Text("Contraseña") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
        Button(onClick = { vm.iniciar(correo, clave) }, enabled = !cargando, modifier = Modifier.padding(top = 16.dp)) {
            Text(if (cargando) "Ingresando..." else "Ingresar")
        }
    }
}
