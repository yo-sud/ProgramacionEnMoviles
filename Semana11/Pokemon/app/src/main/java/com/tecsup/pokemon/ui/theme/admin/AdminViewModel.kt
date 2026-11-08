package com.tecsup.pokemon.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.notification.Notificador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminStats(val guardados: Int = 0, val favoritos: Int = 0)

class AdminViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {
    private val _stats = MutableStateFlow(AdminStats())
    val stats: StateFlow<AdminStats> = _stats.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init { actualizar() }

    private fun actualizar() {
        viewModelScope.launch { _stats.value = AdminStats(repo.cantidadGuardada(), repo.cantidadFavoritos()) }
    }

    fun sincronizar() {
        viewModelScope.launch {
            repo.refrescar()
                .onSuccess { n -> notificador.mostrar("Datos actualizados", "Se guardaron $n elementos"); _mensaje.value = null }
                .onFailure { _mensaje.value = "No se pudo sincronizar" }
            actualizar()
        }
    }

    fun limpiarCache() {
        viewModelScope.launch { repo.limpiarCache(); actualizar() }
    }
}
