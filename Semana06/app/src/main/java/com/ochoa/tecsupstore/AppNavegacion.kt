package com.ochoa.tecsupstore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

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
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route ?: Rutas.INICIO

    // Estado elevado: ids de los productos marcados como favoritos.
    val favoritos = remember { mutableStateListOf<Int>() }
    val onToggleFavorito: (Int) -> Unit = { id ->
        if (id in favoritos) favoritos.remove(id) else favoritos.add(id)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                rutaActual = rutaActual,
                onNavegar = { ruta ->
                    navController.navigate(ruta) { launchSingleTop = true }
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TECSUP Store") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Rutas.INICIO,
                modifier = Modifier.padding(padding)
            ) {
                composable(Rutas.INICIO) {
                    PantallaInicio(
                        favoritos = favoritos,
                        onToggleFavorito = onToggleFavorito
                    )
                }
                composable(Rutas.PEDIDOS) { PantallaMarcador("Mis pedidos") }
                composable(Rutas.FAVORITOS) { PantallaMarcador("Favoritos") }
                composable(Rutas.PERFIL) { PantallaMarcador("Perfil") }
            }
        }
    }
}

@Composable
fun PantallaMarcador(titulo: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(titulo)
    }
}