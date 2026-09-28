package com.ochoa.clinicasalud.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: Double,
    val descripcion: String
)

data class Cita(
    val medico: Medico,
    val fecha: String,
    val hora: String,
    var estado: String = "Confirmada"
)

val medicosDeEjemplo = listOf(
    Medico(1, "Dra. Lucía Fernández", "Cardiología", 4.8, "Especialista en cardiología clínica con 12 años de experiencia."),
    Medico(2, "Dr. Ricardo Nava", "Cardiología", 4.6, "Enfocado en prevención cardiovascular y electrocardiogramas."),
    Medico(3, "Dra. Paola Méndez", "Pediatría", 4.9, "Pediatra especializada en control de niño sano y vacunación."),
    Medico(4, "Dr. Jorge Salinas", "Pediatría", 4.7, "Atención pediátrica general y urgencias infantiles."),
    Medico(5, "Dra. Carmen Rojas", "Dermatología", 4.5, "Especialista en dermatología clínica y estética."),
)