package com.ochoa.clinicasalud.navigation

sealed class Pantalla(val ruta: String) {
    object Inicio : Pantalla("inicio")
    object PerfilMedico : Pantalla("perfil_medico/{medicoId}") {
        fun crearRuta(medicoId: Int) = "perfil_medico/$medicoId"
    }
    object AgendarCita : Pantalla("agendar_cita/{medicoId}") {
        fun crearRuta(medicoId: Int) = "agendar_cita/$medicoId"
    }
    object Confirmacion : Pantalla("confirmacion/{medicoId}/{fecha}/{hora}") {
        fun crearRuta(medicoId: Int, fecha: String, hora: String) = "confirmacion/$medicoId/$fecha/$hora"
    }
    object MisCitas : Pantalla("mis_citas")
    object HistorialMedico : Pantalla("historial_medico")
}