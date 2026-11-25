package com.tecsup.avisoscampus

import android.app.Application
import com.tecsup.avisoscampus.notificacion.Notificador

// [G] Application: crea el canal
class AvisosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Notificador.crearCanal(this)
    }
}
