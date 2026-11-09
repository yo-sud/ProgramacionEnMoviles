package com.tecsup.pokemon.ui.favoritos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ItemFila

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(vm: FavoritosViewModel, onItem: (String) -> Unit, onVolver: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favoritos") },
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
                is UiState.Error -> Text(s.mensaje, Modifier.padding(16.dp))
                is UiState.Exito ->
                    if (s.datos.isEmpty()) Text("Aún no tienes favoritos", Modifier.padding(16.dp))
                    else LazyColumn {
                        items(s.datos, key = { it.id }) { item ->
                            ItemFila(item, { onItem(item.id) }, { vm.alternarFavorito(item.id) })
                        }
                    }
            }
        }
    }
}