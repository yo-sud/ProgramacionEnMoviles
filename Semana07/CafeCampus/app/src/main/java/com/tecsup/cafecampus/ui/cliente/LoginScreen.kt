package com.tecsup.cafecampus.ui.cliente

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.SesionViewModel

/**
 * Un solo Composable para ambos perfiles. La pestaña elegida cambia el tema (CafeTheme):
 * naranja para el Estudiante, índigo para el Administrador.
 */
@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onIngreso: (Rol) -> Unit,
    onOlvidePassword: () -> Unit = {},
    onRegistro: () -> Unit = {}
) {
    val oscuro by vm.modoOscuro.collectAsStateWithLifecycle()
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    var tab by rememberSaveable { mutableIntStateOf(0) }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    val rol = Rol.entries[tab] // 0 = CLIENTE (Estudiante), 1 = ADMIN

    // Cuando el ViewModel confirma el usuario, la pantalla avisa a la navegación.
    LaunchedEffect(usuario) { usuario?.let { onIngreso(Rol.valueOf(it.rol)) } }

    CafeTheme(rol = rol, oscuro = oscuro) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(48.dp))
                Text("Bienvenido", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("Ingresa para continuar", color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(Modifier.height(24.dp))
                SelectorRol(
                    seleccion = tab,
                    opciones = listOf("Estudiante", "Administrador"),
                    onSelecciona = { tab = it; vm.limpiarError() }
                )

                Spacer(Modifier.height(28.dp))
                CafeTextField(
                    valor = correo,
                    onValor = { correo = it; vm.limpiarError() },
                    etiqueta = "Correo",
                    teclado = KeyboardType.Email,
                    icono = Icons.Outlined.Email,
                    error = error != null,
                    accion = ImeAction.Next
                )
                Spacer(Modifier.height(12.dp))
                CafeTextField(
                    valor = clave,
                    onValor = { clave = it; vm.limpiarError() },
                    etiqueta = "Contraseña",
                    esClave = true,
                    icono = Icons.Outlined.Lock,
                    error = error != null,
                    accion = ImeAction.Done,
                    alAccion = { vm.login(correo, clave, rol) }
                )
                error?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onOlvidePassword) { EnlaceTexto("¿Olvidaste tu contraseña?") }
                }

                Spacer(Modifier.height(8.dp))
                CafeButton("Ingresar", onClick = { vm.login(correo, clave, rol) })

                // El registro es solo para estudiantes: el administrador no se crea desde la app
                if (tab == 0) {
                    Spacer(Modifier.height(16.dp))
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        TextButton(onClick = onRegistro) { EnlaceTexto("¿No tienes cuenta?  Regístrate") }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun EnlaceTexto(texto: String) {
    Text(
        texto,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold
    )
}

/** Selector tipo "píldora": la opción activa se pinta con el color primario del tema. */
@Composable
private fun SelectorRol(seleccion: Int, opciones: List<String>, onSelecciona: (Int) -> Unit) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colores.onSurface.copy(alpha = 0.06f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        opciones.forEachIndexed { i, texto ->
            val activo = i == seleccion
            val fondo by animateColorAsState(if (activo) colores.primary else Color.Transparent, label = "fondo")
            val letra by animateColorAsState(if (activo) colores.onPrimary else colores.onSurfaceVariant, label = "letra")
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(fondo)
                    .selectable(selected = activo, onClick = { onSelecciona(i) }, role = Role.Tab)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(texto, color = letra, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}