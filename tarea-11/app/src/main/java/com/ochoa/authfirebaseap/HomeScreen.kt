package com.ochoa.authfirebaseap

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    val email = FirebaseAuth.getInstance().currentUser?.email ?: "Usuario"

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido/a 🎉", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Sesión iniciada como: $email", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Cerrar sesión")
        }
    }
}