package com.ochoa.tecsupstore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Rutas {
    const val INICIO = "inicio"
    const val PEDIDOS = "pedidos"
    const val FAVORITOS = "favoritos"
    const val PERFIL = "perfil"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopAppBar(title = { Text("TECSUP Store") }) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.INICIO,
            modifier = Modifier.padding(padding)
        ) {
            composable(Rutas.INICIO) { PantallaInicio() }
            composable(Rutas.PEDIDOS) { PantallaMarcador("Mis pedidos") }
            composable(Rutas.FAVORITOS) { PantallaMarcador("Favoritos") }
            composable(Rutas.PERFIL) { PantallaMarcador("Perfil") }
        }
    }
}

@Composable
fun PantallaMarcador(titulo: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(titulo)
    }
}