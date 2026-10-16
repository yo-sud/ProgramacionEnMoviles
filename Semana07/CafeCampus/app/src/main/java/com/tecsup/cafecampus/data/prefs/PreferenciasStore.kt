package com.tecsup.cafecampus.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tecsup.cafecampus.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Declarado UNA sola vez, a nivel de archivo.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("ajustes")

/** Preferencias pequeñas (clave-valor). Los pedidos y productos NO van aquí: van en Room. */
class PreferenciasStore(private val context: Context) {

    private object Claves {
        val PERFIL = stringPreferencesKey("perfilActivo")
        val OSCURO = booleanPreferencesKey("modoOscuro")
        val RECIBIR = booleanPreferencesKey("recibirPedidos")
    }

    private val datos: Flow<Preferences> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }

    // ---- Lectura: un Flow por preferencia ----
    val perfilActivo: Flow<String> = datos.map { it[Claves.PERFIL] ?: "" }
    val modoOscuro: Flow<Boolean> = datos.map { it[Claves.OSCURO] ?: false }
    val recibirPedidos: Flow<Boolean> = datos.map { it[Claves.RECIBIR] ?: true }

    // ---- Escritura: funciones suspend ----
    suspend fun guardarPerfil(rol: Rol) { context.dataStore.edit { it[Claves.PERFIL] = rol.name } }
    suspend fun guardarModoOscuro(b: Boolean) { context.dataStore.edit { it[Claves.OSCURO] = b } }
    suspend fun guardarRecibirPedidos(b: Boolean) { context.dataStore.edit { it[Claves.RECIBIR] = b } }
    suspend fun cerrarSesion() { context.dataStore.edit { it[Claves.PERFIL] = "" } }
}