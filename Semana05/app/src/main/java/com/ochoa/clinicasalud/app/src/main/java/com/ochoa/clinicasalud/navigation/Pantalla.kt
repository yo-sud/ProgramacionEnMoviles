package com.ochoa.clinicasalud.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Pantalla(
    val ruta: String,
    val etiqueta: String? = null,
    val icono: ImageVector? = null
) {
    object Inicio : Pantalla("inicio", "Inicio", Icons.Filled.Home)
    object PerfilMedico : Pantalla("perfil_medico/{medicoId}") {
        fun crearRuta(medicoId: Int) = "perfil_medico/$medicoId"
    }
    object AgendarCita : Pantalla("agendar_cita/{medicoId}") {
        fun crearRuta(medicoId: Int) = "agendar_cita/$medicoId"
    }
    object Confirmacion : Pantalla("confirmacion/{medicoId}/{fecha}/{hora}") {
        fun crearRuta(medicoId: Int, fecha: String, hora: String) = "confirmacion/$medicoId/$fecha/$hora"
    }
    object MisCitas : Pantalla("mis_citas", "Mis citas", Icons.Filled.CalendarMonth)
    object HistorialMedico : Pantalla("historial_medico", "Historial médico", Icons.Filled.History)

    companion object {
        val itemsDrawer = listOf(Inicio, MisCitas, HistorialMedico)
    }
}