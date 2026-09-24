package com.ochoa.tecsupfit.navigation

sealed class Pantalla(val ruta: String) {
    object Inicio : Pantalla("inicio")
    object Detalle : Pantalla("detalle/{claseId}") {
        fun crearRuta(claseId: Int) = "detalle/$claseId"
    }
    object Confirmacion : Pantalla("confirmacion/{claseId}/{horario}") {
        fun crearRuta(claseId: Int, horario: String) = "confirmacion/$claseId/$horario"
    }
    object Reservas : Pantalla("reservas")
    object Perfil : Pantalla("perfil")
}