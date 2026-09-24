package com.ochoa.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ochoa.tecsupfit.model.clasesDeEjemplo
import com.ochoa.tecsupfit.ui.screens.DetalleScreen
import com.ochoa.tecsupfit.ui.screens.InicioScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Pantalla.Inicio.ruta,
        modifier = modifier
    ) {
        composable(Pantalla.Inicio.ruta) {
            InicioScreen(
                onClaseClick = { clase ->
                    navController.navigate(Pantalla.Detalle.crearRuta(clase.id))
                }
            )
        }

        composable(
            route = Pantalla.Detalle.ruta,
            arguments = listOf(navArgument("claseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 0
            val clase = clasesDeEjemplo.first { it.id == claseId }

            DetalleScreen(
                clase = clase,
                onReservarClick = {
                    // TODO Commit 6: navegar a Confirmacion con el horario elegido
                }
            )
        }
    }
}