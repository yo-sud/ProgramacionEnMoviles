package com.ochoa.lab05.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class UserProfile(
    val fullName: String,
    val email: String,
    val phone: String,
    val career: String,
    val cycle: String
) {
    val initials: String
        get() = fullName.split(" ").filter { it.isNotBlank() }.take(2)
            .joinToString("") { it.first().uppercase() }
}

// Datos de ejemplo: reemplázalos por los reales
val currentUser = UserProfile(
    fullName = "Yamil Ochoa",
    email = "yamil.ochoa@tecsup.edu.pe",
    phone = "+51 999 999 999",
    career = "Ingeniería de Software",
    cycle = "7mo ciclo"
)

/** Correo con el que se inició sesión (con dominio). */
object Session {
    var email by mutableStateOf(currentUser.email)
}