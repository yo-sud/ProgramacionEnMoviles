package com.tecsup.pokemon.ui.lista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView
import com.tecsup.pokemon.ui.components.ItemFila

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

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Row {
            TextButton(onFavoritos) { Text("Favoritos") }
            if (esAdmin) TextButton(onAdmin) { Text("Panel Admin") }
            Spacer(Modifier.weight(1f))
            TextButton(onSalir) { Text("Cerrar sesión") }
        }
        aviso?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp)) }
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
