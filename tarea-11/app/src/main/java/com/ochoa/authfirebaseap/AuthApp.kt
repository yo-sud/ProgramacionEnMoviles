package com.ochoa.authfirebaseap

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthApp() {
    val nav = rememberNavController()
    val start = if (FirebaseAuth.getInstance().currentUser != null) "home" else "login"

    NavHost(navController = nav, startDestination = start) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { nav.navigate("home") { popUpTo("login") { inclusive = true } } },
                onGoRegister = { nav.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { nav.navigate("home") { popUpTo("login") { inclusive = true } } },
                onBackToLogin = { nav.popBackStack() }
            )
        }
        composable("home") {
            HomeScreen(onLogout = {
                FirebaseAuth.getInstance().signOut()
                nav.navigate("login") { popUpTo("home") { inclusive = true } }
            })
        }
    }
}