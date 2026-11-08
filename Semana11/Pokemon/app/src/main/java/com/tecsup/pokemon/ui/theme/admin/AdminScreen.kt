package com.tecsup.pokemon.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminScreen(vm: AdminViewModel, onVolver: () -> Unit) {
    val stats by vm.stats.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
        TextButton(onVolver) { Text("← Volver") }
        Text("Total guardado: ${stats.guardados}")
        Text("Total favoritos: ${stats.favoritos}")
        mensaje?.let { Text(it) }
        Button(vm::sincronizar, Modifier.padding(top = 16.dp)) { Text("Sincronizar") }
        OutlinedButton(vm::limpiarCache, Modifier.padding(top = 8.dp)) { Text("Limpiar caché") }
    }
}
