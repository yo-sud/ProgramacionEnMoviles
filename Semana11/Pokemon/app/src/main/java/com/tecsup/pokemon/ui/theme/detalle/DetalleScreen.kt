package com.tecsup.pokemon.ui.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView

@Composable
fun DetalleScreen(vm: DetalleViewModel, id: String, onVolver: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    LaunchedEffect(id) { vm.cargar(id) }

    Column(Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
        TextButton(onVolver) { Text("← Volver") }
        when (val s = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(s.mensaje) { vm.cargar(id) }
            is UiState.Exito -> {
                val p = s.datos
                AsyncImage(p.imagenUrl, p.titulo, Modifier.size(160.dp))
                Text(p.titulo, style = MaterialTheme.typography.headlineMedium)
                Text("Tipos: ${p.subtitulo}")
                Text(p.detalle, modifier = Modifier.padding(top = 8.dp))
                Button(vm::alternarFavorito, Modifier.padding(top = 16.dp)) {
                    Text(if (p.esFavorito) "Quitar de favoritos" else "Marcar favorito")
                }
            }
        }
    }
}
