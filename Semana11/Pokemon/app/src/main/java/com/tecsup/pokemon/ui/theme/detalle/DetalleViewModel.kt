package com.tecsup.pokemon.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(private val repo: ItemRepository) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()
    private var idActual: String? = null

    fun cargar(id: String) {
        if (id == idActual && _estado.value is UiState.Exito) return
        idActual = id
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repo.obtenerDetalle(id)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error("No se pudo cargar el detalle") }
        }
    }

    fun alternarFavorito() {
        val id = idActual ?: return
        viewModelScope.launch {
            repo.alternarFavorito(id)
            (_estado.value as? UiState.Exito)?.let {
                _estado.value = UiState.Exito(it.datos.copy(esFavorito = !it.datos.esFavorito))
            }
        }
    }
}
