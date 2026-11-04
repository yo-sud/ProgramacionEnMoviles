package com.ochoa.authfirebaseap

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onGoRegister: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Text("Programación Móvil - Tecsup", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.weight(1f))

        Text("Iniciar Sesión 🔐", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true, visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    error = "Completa todos los campos"; return@Button
                }
                loading = true; error = null
                auth.signInWithEmailAndPassword(email.trim(), password)
                    .addOnCompleteListener { task ->
                        loading = false
                        if (task.isSuccessful) onLoginSuccess()
                        else error = task.exception?.message ?: "Error al ingresar"
                    }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (loading) "Ingresando..." else "Ingresar") }

        TextButton(onClick = onGoRegister) { Text("¿No tienes cuenta? Regístrate aquí") }

        Spacer(Modifier.weight(1f))
        Text("Juan León S. - Tecsup", style = MaterialTheme.typography.bodySmall)
    }
}