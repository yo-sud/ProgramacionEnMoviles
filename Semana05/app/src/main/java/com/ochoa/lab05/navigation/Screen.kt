package com.ochoa.lab05.navigation

import androidx.navigation.NavController

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object StudentList : Screen("student_list")
    object MyProfile : Screen("my_profile")
    object StudentDetail : Screen("student_detail/{studentId}") {
        fun createRoute(studentId: Int): String = "student_detail/$studentId"
    }
}

fun NavController.logout() {
    navigate(Screen.Login.route) {
        popUpTo(0) { inclusive = true }
    }
}