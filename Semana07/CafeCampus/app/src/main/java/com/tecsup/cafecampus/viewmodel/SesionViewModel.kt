package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: CafeRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuario.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val recibirPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        viewModelScope.launch { restaurar() }
    }

    private fun credencialesDemo(rol: Rol): Pair<String, String> =
        if (rol == Rol.ADMIN) "admin@cafe.test" to "admin" else "juan@campus.test" to "1234"

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa el correo y la contraseña"
            return
        }
        viewModelScope.launch {
            val u = repo.login(correo.trim(), clave)
            when {
                u == null -> _error.value = "Correo o contraseña incorrectos"
                u.rol != rolSeleccionado.name ->
                    _error.value = "Esta cuenta no pertenece al perfil ${if (rolSeleccionado == Rol.ADMIN) "Administrador" else "Estudiante"}"
                else -> {
                    prefs.guardarPerfil(rolSeleccionado)
                    _error.value = null
                    _usuario.value = u
                }
            }
        }
    }

    fun limpiarError() { _error.value = null }

    /** Lee el perfil guardado, carga su usuario y devuelve el rol (null si no hay sesión guardada). */
    suspend fun restaurar(): Rol? {
        val guardado = prefs.perfilActivo.first()
        val rol = Rol.entries.firstOrNull { it.name == guardado } ?: return null
        if (_usuario.value?.rol == rol.name) return rol
        val (correo, clave) = credencialesDemo(rol)
        val u = repo.login(correo, clave) ?: return null
        _usuario.value = u
        return rol
    }

    fun cerrarSesion(onListo: () -> Unit) {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuario.value = null
            _error.value = null
            onListo()
        }
    }
}