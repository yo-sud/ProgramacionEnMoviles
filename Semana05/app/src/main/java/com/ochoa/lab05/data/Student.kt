package com.ochoa.lab05.data

import androidx.compose.ui.graphics.Color
import com.ochoa.lab05.ui.theme.AppColors

/**
 * Modelo de un alumno del directorio.
 * avatarColor: color sólido usado como "portada" detrás del marco
 * circular de la foto (simula la imagen de perfil).
 */
data class Student(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val career: String,
    val email: String,
    val faculty: String,
    val bio: String,
    val avatarColor: Color
)

val sampleStudents = listOf(
    Student(
        id = 1,
        firstName = "Yamil",
        lastName = "Ochoa",
        career = "Ingeniería de Software",
        email = "yamil.ochoa@tecsup.edu.pe",
        faculty = "Facultad de Ingeniería",
        bio = "Estudiante apasionado por el desarrollo móvil y la arquitectura de software.",
        avatarColor = AppColors.AvatarPalette[0]
    ),
    Student(
        id = 2,
        firstName = "Lucía",
        lastName = "Fernández",
        career = "Ingeniería de Sistemas",
        email = "lucia.fernandez@tecsup.edu.pe",
        faculty = "Facultad de Ingeniería",
        bio = "Interesada en ciencia de datos e inteligencia artificial.",
        avatarColor = AppColors.AvatarPalette[1]
    ),
    Student(
        id = 3,
        firstName = "Diego",
        lastName = "Ramírez",
        career = "Diseño Gráfico",
        email = "diego.ramirez@tecsup.edu.pe",
        faculty = "Facultad de Diseño",
        bio = "Enfocado en experiencia de usuario (UI/UX) y branding digital.",
        avatarColor = AppColors.AvatarPalette[2]
    ),
    Student(
        id = 4,
        firstName = "Camila",
        lastName = "Torres",
        career = "Administración de Empresas",
        email = "camila.torres@tecsup.edu.pe",
        faculty = "Facultad de Negocios",
        bio = "Le gusta liderar proyectos estudiantiles y emprendimiento.",
        avatarColor = AppColors.AvatarPalette[3]
    ),
    Student(
        id = 5,
        firstName = "Mateo",
        lastName = "Quispe",
        career = "Ingeniería Industrial",
        email = "mateo.quispe@tecsup.edu.pe",
        faculty = "Facultad de Ingeniería",
        bio = "Apasionado por la optimización de procesos productivos.",
        avatarColor = AppColors.AvatarPalette[4]
    ),
    Student(
        id = 6,
        firstName = "Valentina",
        lastName = "Cruz",
        career = "Ingeniería de Software",
        email = "valentina.cruz@tecsup.edu.pe",
        faculty = "Facultad de Ingeniería",
        bio = "Desarrolladora frontend con interés en Jetpack Compose Multiplatform.",
        avatarColor = AppColors.AvatarPalette[5]
    )
)