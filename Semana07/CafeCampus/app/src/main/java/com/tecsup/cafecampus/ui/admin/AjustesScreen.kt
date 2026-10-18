package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.EtiquetaChip
import com.tecsup.cafecampus.viewmodel.SesionViewModel

/**
 * Se usa en los dos perfiles: es "Ajustes" para el administrador y "Perfil" para el estudiante.
 * "Recibir pedidos" solo aparece para el administrador.
 */
@Composable
fun AjustesScreen(
    vm: SesionViewModel,
    onCambioPerfil: (Rol) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()
    val oscuro by vm.modoOscuro.collectAsStateWithLifecycle()
    val recibir by vm.recibirPedidos.collectAsStateWithLifecycle()
    val esAdmin = usuario?.rol == Rol.ADMIN.name

    Column(Modifier.fillMaxSize()) {
        AppTopBar(if (esAdmin) "Ajustes" else "Perfil")

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CafeCard {
                Text(usuario?.nombre ?: "", style = MaterialTheme.typography.titleLarge)
                Text(usuario?.correo ?: "", color = MaterialTheme.colorScheme.onSurfaceVariant)
                EtiquetaChip(
                    texto = if (esAdmin) "Administrador" else "Estudiante",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            CafeCard(espaciado = 12) {
                FilaSwitch(
                    titulo = "Modo oscuro",
                    detalle = "Cambia el tema de toda la app",
                    marcado = oscuro,
                    onCambio = { vm.setModoOscuro(it) }
                )
                if (esAdmin) {
                    FilaSwitch(
                        titulo = "Recibir pedidos",
                        detalle = "Si lo apagas, los estudiantes verán \"Cafetería cerrada\"",
                        marcado = recibir,
                        onCambio = { vm.setRecibirPedidos(it) }
                    )
                }
            }

            // Modo práctica: ambos perfiles conviven en el mismo dispositivo
            CafeButton(
                texto = if (esAdmin) "Cambiar a perfil Estudiante" else "Cambiar a perfil Administrador",
                secundario = true,
                onClick = { vm.cambiarPerfil(onCambioPerfil) }
            )
            CafeButton(texto = "Cerrar sesión", onClick = { vm.cerrarSesion(onCerrarSesion) })
        }
    }
}

@Composable
private fun FilaSwitch(titulo: String, detalle: String, marcado: Boolean, onCambio: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = marcado, onCheckedChange = onCambio)
    }
}