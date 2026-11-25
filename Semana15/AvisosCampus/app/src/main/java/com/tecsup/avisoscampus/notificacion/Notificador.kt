package com.tecsup.avisoscampus.notificacion

import android.content.Context
import com.tecsup.avisoscampus.model.Aviso

// [G] canal, permiso y mostrar
object Notificador {
    const val CANAL = "avisos"

    // Paso 1
    fun crearCanal(context: Context) {
        // TODO: si SDK_INT >= O, crear NotificationChannel(CANAL, "Avisos", IMPORTANCE_DEFAULT) y registrarlo
    }

    // Paso 2
    fun tienePermiso(context: Context): Boolean {
        // TODO: si SDK_INT < 33 -> true; si no, ContextCompat.checkSelfPermission(...)
        return false
    }

    // Pasos 3 y 4
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO 1: si no tienePermiso(context) -> return false
        // TODO 2: NotificationCompat.Builder(context, CANAL) + PendingIntent (Paso 4)
        // TODO 3: NotificationManagerCompat.from(context).notify(aviso.id, ...)
        return true
    }
}
