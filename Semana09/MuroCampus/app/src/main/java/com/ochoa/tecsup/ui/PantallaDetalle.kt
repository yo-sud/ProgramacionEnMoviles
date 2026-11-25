package com.ochoa.tecsup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ochoa.tecsup.viewmodel.PostsViewModel
import com.ochoa.tecsup.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, onReintentar: () -> Unit, modifier: Modifier = Modifier) {
    val estado by vm.detalle.collectAsStateWithLifecycle()

    when (val e = estado) {
        UiState.Cargando -> Cargando("Cargando detalle…", modifier)
        is UiState.Error -> ErrorConReintento(e.mensaje, onReintentar, modifier)
        is UiState.Exito -> LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(e.datos.post.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(e.datos.post.body, style = MaterialTheme.typography.bodyLarge)
                    HorizontalDivider()
                    Text(
                        "Comentarios (${e.datos.comentarios.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            items(e.datos.comentarios, key = { it.id }) { c ->
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(c.email, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
                    Text(c.body, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}