package com.ochoa.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ochoa.centrotareas.data.TareasRepository
import com.ochoa.centrotareas.model.CargaUi
import com.ochoa.centrotareas.model.PrimosUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    // ----- Tarea con progreso (pasos 1 a 4)
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    fun tareaLarga() {
        viewModelScope.launch {
            _progreso.value = 0f
            _mensaje.value = "Trabajando..."
            for (i in 1..100) {
                withContext(Dispatchers.Default) { delay(40) }
                _progreso.value = i / 100f
            }
            _mensaje.value = "Terminó"
        }
    }
    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()
    // TODO Paso 4: trabajo (Job), corriendo y cancelar()

    // ----- Contar primos (paso 5)
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    // TODO Paso 5: fun contar(hasta: Int) y fun cancelarPrimos()

    // ----- Dos cargas (pasos 6 y 7)
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    // TODO Paso 6: fun cargarUnaPorUna() y fun cargarALaVez()
    // TODO Paso 7: simularError y manejo de errores
}