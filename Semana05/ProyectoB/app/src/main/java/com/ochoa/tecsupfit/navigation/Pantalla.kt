package com.ochoa.tecsupfit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Pantalla(
    val ruta: String,
    val etiqueta: String? = null,
    val icono: ImageVector? = null
) {
    object Inicio : Pantalla("inicio", "Inicio", Icons.Filled.Home)
    object Detalle : Pantalla("detalle/{claseId}") {
        fun crearRuta(claseId: Int) = "detalle/$claseId"
    }
    object Confirmacion : Pantalla("confirmacion/{claseId}/{horario}") {
        fun crearRuta(claseId: Int, horario: String) = "confirmacion/$claseId/$horario"
    }
    object Reservas : Pantalla("reservas", "Reservas", Icons.Filled.DateRange)
    object Rutinas : Pantalla("rutinas", "Rutinas", Icons.Filled.FitnessCenter)
    object Perfil : Pantalla("perfil", "Perfil", Icons.Filled.Person)

    companion object {
        val itemsBottomBar = listOf(Inicio, Reservas, Rutinas, Perfil)
    }
}