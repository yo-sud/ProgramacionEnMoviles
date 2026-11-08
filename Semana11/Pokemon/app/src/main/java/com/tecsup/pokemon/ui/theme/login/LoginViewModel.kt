package com.tecsup.pokemon.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.data.auth.AuthRepository
import com.tecsup.pokemon.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val auth: AuthRepository) : ViewModel() {
    private val _sesion = MutableStateFlow<Sesion?>(null)
    val sesion: StateFlow<Sesion?> = _sesion.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    init { viewModelScope.launch { _sesion.value = auth.sesionActual() } } // R11: sesión persistente

    fun iniciar(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) { _error.value = "Completa correo y contraseña"; return }
        viewModelScope.launch {
            _cargando.value = true
            auth.iniciarSesion(correo.trim(), clave)
                .onSuccess { _sesion.value = it; _error.value = null }
                .onFailure { _error.value = it.message ?: "No se pudo iniciar sesión" }
            _cargando.value = false
        }
    }

    fun cerrarSesion() { auth.cerrarSesion(); _sesion.value = null }
}
