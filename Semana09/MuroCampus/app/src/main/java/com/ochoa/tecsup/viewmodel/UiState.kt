package com.ochoa.tecsup.viewmodel

// import kotlinx.serialization.SerializationException   // Paso 1: descomenta
// import retrofit2.HttpException                         // Paso 1: descomenta
// import java.io.IOException                             // Paso 1: descomenta

/** Los tres estados que dibuja cada pantalla: Cargando, Éxito y Error. */
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

// Paso 1: cuando agregues las dependencias, descomenta los import de arriba y esta función.
// Convierte una excepción en un mensaje para el usuario (se usa en el ViewModel, Paso 6).
//
// fun mensajeDe(e: Throwable): String = when (e) {
//     is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
//     is HttpException -> "Error del servidor (${e.code()})"
//     is SerializationException -> "Respuesta inesperada"
//     else -> "Error desconocido"
// }
