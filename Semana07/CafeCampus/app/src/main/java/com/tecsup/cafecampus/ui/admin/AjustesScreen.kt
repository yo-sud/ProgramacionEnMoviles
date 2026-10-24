package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeCard
import com.tecsup.cafecampus.ui.components.EtiquetaChip
import com.tecsup.cafecampus.viewmodel.SesionViewModel

/**
 * Se usa en los dos perfiles: "Ajustes" para el administrador y "Perfil" para el estudiante.
 * "Notificaciones" y "Recibir pedidos" solo aparecen para el administrador.
 */
@Composable
fun AjustesScreen(
    vm: SesionViewModel,
    onCambioPerfil: (Rol) -> Unit,
    onCerrarSesion: () -> Unit,
    onVolver: (() -> Unit)? = null
) {
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()
    val oscuro by vm.modoOscuro.collectAsStateWithLifecycle()
    val recibir by vm.recibirPedidos.collectAsStateWithLifecycle()
    val notificaciones by vm.notificaciones.collectAsStateWithLifecycle()
    val esAdmin = usuario?.rol == Rol.ADMIN.name
    val c = MaterialTheme.colorScheme

    Column(Modifier.fillMaxSize()) {
        AppTopBar(if (esAdmin) "Ajustes" else "Perfil", onVolver = onVolver, centrado = true)

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Usuario con sesión iniciada
            CafeCard {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        Modifier.size(56.dp).clip(CircleShape).background(c.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Person, contentDescription = null, tint = c.primary, modifier = Modifier.size(30.dp))
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            usuario?.nombre.orEmpty(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            usuario?.correo.orEmpty(),
                            style = MaterialTheme.typography.bodySmall,
                            color = c.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        EtiquetaChip("Perfil activo", c.primary)
                    }
                }
            }

            // Modo práctica: ambos perfiles conviven en el mismo dispositivo
            TarjetaCambioPerfil(
                detalle = if (esAdmin) "Modo práctica: ir a vista Estudiante" else "Modo práctica: ir a vista Administrador",
                onClick = { vm.cambiarPerfil(onCambioPerfil) }
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Preferencias", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                CafeCard(espaciado = 12) {
                    FilaSwitch(
                        titulo = "Modo oscuro",
                        detalle = "Cambia el tema de la app",
                        marcado = oscuro,
                        onCambio = { vm.setModoOscuro(it) }
                    )
                    if (esAdmin) {
                        HorizontalDivider(color = c.outlineVariant)
                        FilaSwitch(
                            titulo = "Notificaciones",
                            detalle = "Avisar nuevos pedidos",
                            marcado = notificaciones,
                            onCambio = { vm.setNotificaciones(it) }
                        )
                        HorizontalDivider(color = c.outlineVariant)
                        FilaSwitch(
                            titulo = "Recibir pedidos",
                            detalle = "Abrir o cerrar la cafetería",
                            marcado = recibir,
                            onCambio = { vm.setRecibirPedidos(it) }
                        )
                    }
                }
            }

            Surface(modifier = Modifier.fillMaxWidth(), color = c.primaryContainer.copy(alpha = 0.6f), shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Se guardan en el dispositivo", style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant)
                    Text("con DataStore", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = c.primary)
                }
            }

            CafeButton(texto = "Cerrar sesión", secundario = true, peligro = true, onClick = { vm.cerrarSesion(onCerrarSesion) })
        }
    }
}

@Composable
private fun TarjetaCambioPerfil(detalle: String, onClick: () -> Unit) {
    val c = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(c.primary)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = c.onPrimary, modifier = Modifier.size(28.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Cambiar de perfil", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = c.onPrimary)
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = c.onPrimary.copy(alpha = 0.85f))
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = c.onPrimary)
    }
}

@Composable
private fun FilaSwitch(titulo: String, detalle: String, marcado: Boolean, onCambio: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = marcado, onCheckedChange = onCambio)
    }
}