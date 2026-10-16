package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.SesionViewModel

/** Un solo Composable para ambos perfiles: la pestaña elegida cambia el color del botón. */
@Composable
fun LoginScreen(vm: SesionViewModel, onIngreso: (Rol) -> Unit) {
    val oscuro by vm.modoOscuro.collectAsStateWithLifecycle()
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    var tab by rememberSaveable { mutableIntStateOf(0) }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    val rol = Rol.entries[tab] // 0 = CLIENTE (Estudiante), 1 = ADMIN

    LaunchedEffect(usuario) { usuario?.let { onIngreso(Rol.valueOf(it.rol)) } }

    CafeTheme(rol = rol, oscuro = oscuro) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))
                Icon(Icons.Filled.LocalCafe, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
                Text("CafeCampus", style = MaterialTheme.typography.headlineMedium)
                Text("Pedidos de la cafetería del campus", color = MaterialTheme.colorScheme.onSurfaceVariant)

                TabRow(selectedTabIndex = tab) {
                    Tab(selected = tab == 0, onClick = { tab = 0; vm.limpiarError() }, text = { Text("Estudiante") })
                    Tab(selected = tab == 1, onClick = { tab = 1; vm.limpiarError() }, text = { Text("Administrador") })
                }

                CafeTextField(
                    valor = correo, onValor = { correo = it; vm.limpiarError() },
                    etiqueta = "Correo", teclado = KeyboardType.Email, error = error != null
                )
                CafeTextField(
                    valor = clave, onValor = { clave = it; vm.limpiarError() },
                    etiqueta = "Contraseña", esClave = true, error = error != null
                )
                error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium) }

                CafeButton("Ingresar", onClick = { vm.login(correo, clave, rol) })

                Text(
                    "Datos de prueba: juan@campus.test / 1234  ·  admin@cafe.test / admin",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}