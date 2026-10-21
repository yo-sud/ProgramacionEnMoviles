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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.SesionViewModel

/** Un solo Composable para ambos perfiles: la pestaña elegida cambia el color de toda la pantalla. */
@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onIngreso: (Rol) -> Unit,
    onOlvide: () -> Unit = {},
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
        val colores = MaterialTheme.colorScheme
        Surface(Modifier.fillMaxSize(), color = colores.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, top = 56.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Bienvenido", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Ingresa para continuar", color = colores.onSurfaceVariant)
                }

                Spacer(Modifier.height(4.dp))
                SelectorRol(seleccion = tab, onSeleccion = { tab = it; vm.limpiarError() })
                Spacer(Modifier.height(4.dp))

                CafeTextField(
                    valor = correo, onValor = { correo = it; vm.limpiarError() },
                    etiqueta = "Correo", teclado = KeyboardType.Email,
                    error = error != null, icono = Icons.Outlined.Email
                )
                CafeTextField(
                    valor = clave, onValor = { clave = it; vm.limpiarError() },
                    etiqueta = "Contraseña", esClave = true,
                    error = error != null, icono = Icons.Outlined.Lock
                )

                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    TextButton(onClick = onOlvide) {
                        Text("¿Olvidaste tu contraseña?", color = colores.primary, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
                    }
                }

                error?.let { Text(it, color = colores.error, style = MaterialTheme.typography.bodyMedium) }

                CafeButton("Ingresar", onClick = { vm.login(correo, clave, rol) })

                // Las cuentas de administrador no se crean desde la app
                if (rol == Rol.CLIENTE) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        TextButton(onClick = onRegistro) {
                            Text("¿No tienes cuenta?  Regístrate", color = colores.primary, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}

/** Selector tipo "píldora": la opción activa se rellena con el color del perfil. */
@Composable
private fun SelectorRol(seleccion: Int, onSeleccion: (Int) -> Unit) {
    val colores = MaterialTheme.colorScheme
    val opciones = listOf("Estudiante", "Administrador")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colores.onSurface.copy(alpha = 0.06f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        opciones.forEachIndexed { i, texto ->
            val activa = i == seleccion
            val fondo by animateColorAsState(if (activa) colores.primary else Color.Transparent, label = "fondoSelector")
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(fondo)
                    .selectable(selected = activa, onClick = { onSeleccion(i) }, role = Role.Tab),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    texto,
                    fontWeight = FontWeight.SemiBold,
                    color = if (activa) colores.onPrimary else colores.onSurfaceVariant
                )
            }
        }
    }
}