package com.ochoa.tecsupfit.model

data class ClaseGimnasio(
    val id: Int,
    val nombre: String,
    val horario: String,
    val instructor: String,
    val cupoDisponible: Int,
    val categoria: String
)

data class Reserva(
    val clase: ClaseGimnasio,
    val horarioElegido: String,
    var estado: String = "Confirmada"
)

val clasesDeEjemplo = listOf(
    ClaseGimnasio(1, "Spinning", "7:00 AM", "Carla Ruiz", 5, "Hoy"),
    ClaseGimnasio(2, "Funcional", "9:00 AM", "Diego Paz", 8, "Hoy"),
    ClaseGimnasio(3, "Yoga", "6:00 PM", "Mariana Gil", 10, "Hoy"),
    ClaseGimnasio(4, "CrossFit", "8:00 AM", "Luis Torres", 6, "Esta semana"),
    ClaseGimnasio(5, "Zumba", "5:00 PM", "Andrea Soto", 12, "Esta semana"),
)