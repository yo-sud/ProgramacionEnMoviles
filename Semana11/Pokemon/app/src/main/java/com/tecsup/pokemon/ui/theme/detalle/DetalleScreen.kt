package com.tecsup.pokemon.ui.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(vm: DetalleViewModel, id: String, onVolver: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    LaunchedEffect(id) { vm.cargar(id) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val s = estado) {
                UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(s.mensaje) { vm.cargar(id) }
                is UiState.Exito -> {
                    val p = s.datos
                    Column(
                        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(Modifier.fillMaxWidth()) {
                            Column(
                                Modifier.fillMaxWidth().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(p.imagenUrl, p.titulo, Modifier.size(180.dp))
                                Text(p.titulo, style = MaterialTheme.typography.headlineMedium)
                                Row(
                                    Modifier.padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    p.subtitulo.split(" / ").forEach { tipo ->
                                        AssistChip(onClick = {}, label = { Text(tipo) })
                                    }
                                }
                            }
                        }
                        Card(Modifier.fillMaxWidth().padding(top = 12.dp)) {
                            Text(p.detalle, Modifier.padding(16.dp))
                        }
                        Button(onClick = vm::alternarFavorito, modifier = Modifier.padding(top = 16.dp)) {
                            Text(if (p.esFavorito) "Quitar de favoritos" else "Marcar favorito")
                        }
                    }
                }
            }
        }
    }
}