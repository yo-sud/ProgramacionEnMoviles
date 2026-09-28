package com.ochoa.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ochoa.tecsupfit.model.Reserva
import com.ochoa.tecsupfit.navigation.NavGraph
import com.ochoa.tecsupfit.navigation.Pantalla
import com.ochoa.tecsupfit.ui.theme.TecsupfitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TecsupfitTheme {
                val navController = rememberNavController()

                val listaReservas = remember { mutableStateListOf<Reserva>() }

                Scaffold(
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        NavigationBar {
                            Pantalla.itemsBottomBar.forEach { pantalla ->
                                NavigationBarItem(
                                    selected = currentDestination?.hierarchy?.any {
                                        it.route == pantalla.ruta
                                    } == true,
                                    onClick = {
                                        if (currentDestination?.route != pantalla.ruta) {
                                            navController.navigate(pantalla.ruta) {
                                                popUpTo(Pantalla.Inicio.ruta) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    },
                                    icon = { Icon(pantalla.icono!!, contentDescription = pantalla.etiqueta) },
                                    label = { Text(pantalla.etiqueta!!) }
                                )
                            }
                        }
                    }
                ) { paddingInterno ->
                    NavGraph(
                        navController = navController,
                        listaReservas = listaReservas,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingInterno)
                    )
                }
            }
        }
    }
}