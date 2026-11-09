package com.tecsup.pokemon.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(vm: AdminViewModel, onVolver: () -> Unit) {
    val stats by vm.stats.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    var confirmar by rememberSaveable { mutableStateOf(false) }

    if (confirmar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("Limpiar caché") },
            text = { Text("Se borrarán los datos guardados, incluidos los favoritos. ¿Continuar?") },
            confirmButton = {
                TextButton(onClick = { confirmar = false; vm.limpiarCache() }) { Text("Limpiar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmar = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Admin") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(Modifier.weight(1f)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Guardados", style = MaterialTheme.typography.labelLarge)
                        Text("${stats.guardados}", style = MaterialTheme.typography.headlineLarge)
                    }
                }
                Card(Modifier.weight(1f)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Favoritos", style = MaterialTheme.typography.labelLarge)
                        Text("${stats.favoritos}", style = MaterialTheme.typography.headlineLarge)
                    }
                }
            }
            mensaje?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }
            Button(
                onClick = vm::sincronizar,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) { Text("Sincronizar") }
            OutlinedButton(
                onClick = { confirmar = true },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) { Text("Limpiar caché") }
        }
    }
}