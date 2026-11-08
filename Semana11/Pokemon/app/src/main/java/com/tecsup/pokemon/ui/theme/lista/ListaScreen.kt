package com.tecsup.pokemon.ui.lista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView
import com.tecsup.pokemon.ui.components.ItemFila

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    vm: ListaViewModel,
    esAdmin: Boolean,
    onItem: (String) -> Unit,
    onFavoritos: () -> Unit,
    onAdmin: () -> Unit,
    onSalir: () -> Unit
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val aviso by vm.aviso.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokédex") },
                actions = {
                    IconButton(onClick = onFavoritos) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                    }
                    if (esAdmin) IconButton(onClick = onAdmin) {
                        Icon(Icons.Filled.Settings, contentDescription = "Panel Admin")
                    }
                    IconButton(onClick = onSalir) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            aviso?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
            }
            when (val s = estado) {
                UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(s.mensaje, vm::refrescar)
                is UiState.Exito ->
                    if (s.datos.isEmpty()) Text("No hay elementos", Modifier.padding(16.dp))
                    else LazyColumn {
                        items(s.datos, key = { it.id }) { item ->
                            ItemFila(item, { onItem(item.id) }, { vm.alternarFavorito(item.id) })
                        }
                    }
            }
        }
    }
}