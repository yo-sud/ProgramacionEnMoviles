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
fun RegisterScreen(onRegisterSuccess: () -> Unit, onBackToLogin: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear cuenta ✨", style = MaterialTheme.typography.headlineMedium)
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
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = confirm, onValueChange = { confirm = it },
            label = { Text("Confirmar contraseña") },
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
                when {
                    email.isBlank() || password.isBlank() -> error = "Completa todos los campos"
                    password.length < 6 -> error = "La contraseña debe tener mínimo 6 caracteres"
                    password != confirm -> error = "Las contraseñas no coinciden"
                    else -> {
                        loading = true; error = null
                        auth.createUserWithEmailAndPassword(email.trim(), password)
                            .addOnCompleteListener { task ->
                                loading = false
                                if (task.isSuccessful) onRegisterSuccess()
                                else error = task.exception?.message ?: "Error al registrar"
                            }
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (loading) "Registrando..." else "Registrarme") }

        TextButton(onClick = onBackToLogin) { Text("← Volver al login") }
    }
}