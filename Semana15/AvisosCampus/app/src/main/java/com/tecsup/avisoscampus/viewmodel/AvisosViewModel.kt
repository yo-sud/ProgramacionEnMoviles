package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.Origen
import com.tecsup.avisoscampus.model.PermisoUi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

// [G] PROHIBIDO aquí: Context, NotificationManager, BroadcastReceiver
class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {

    val avisos: StateFlow<List<Aviso>> = repo.avisos

    // Evento de una sola vez: "muestra esta notificación".
    // El ViewModel NO tiene Context: lo recibe la pantalla.
    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan: StateFlow<Int?> = _faltan.asStateFlow()
    private var trabajoRecordatorio: Job? = null

    private val _escuchandoAvion = MutableStateFlow(false)
    val escuchandoAvion: StateFlow<Boolean> = _escuchandoAvion.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
    }

    fun avisarAhora() {
        val aviso = repo.registrar(
            titulo = "Aviso del campus",
            texto = "Tienes un nuevo aviso.",
            origen = Origen.BOTON
        )
        viewModelScope.launch { _eventos.emit(aviso) }
    }

    fun recordarEn(segundos: Int) {
        cancelarRecordatorio()
        trabajoRecordatorio = viewModelScope.launch {
            try {
                for (restantes in segundos downTo 1) {
                    _faltan.value = restantes
                    delay(1_000)
                }
                _faltan.value = null
                val aviso = repo.registrar(
                    titulo = "Recordatorio del campus",
                    texto = "Este es tu aviso programado.",
                    origen = Origen.RECORDATORIO
                )
                _eventos.emit(aviso)
            } finally {
                if (trabajoRecordatorio === coroutineContext[Job]) {
                    _faltan.value = null
                    trabajoRecordatorio = null
                }
            }
        }
    }

    fun cancelarRecordatorio() {
        trabajoRecordatorio?.cancel()
        trabajoRecordatorio = null
        _faltan.value = null
    }

    fun cambiarEscucha(valor: Boolean) {
        _escuchandoAvion.value = valor
    }

    fun alCambiarModoAvion(activo: Boolean) {
        if (!activo) return
        val aviso = repo.registrar(
            titulo = "Modo avión activado",
            texto = "Se activó el modo avión en el dispositivo.",
            origen = Origen.MODO_AVION
        )
        viewModelScope.launch { _eventos.emit(aviso) }
    }

    // TODO Paso 8: fun noSePudoMostrar()
}
