package com.ochoa.tecsupfit.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ochoa.tecsupfit.model.clasesDeEjemplo
import com.ochoa.tecsupfit.ui.screens.ConfirmacionScreen
import com.ochoa.tecsupfit.ui.screens.DetalleScreen
import com.ochoa.tecsupfit.ui.screens.InicioScreen
import com.ochoa.tecsupfit.ui.screens.RutinasScreen

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
                onReservarClick = { horarioElegido ->
                    navController.navigate(
                        Pantalla.Confirmacion.crearRuta(clase.id, horarioElegido)
                    )
                }
            )
        }

        composable(
            route = Pantalla.Confirmacion.ruta,
            arguments = listOf(
                navArgument("claseId") { type = NavType.IntType },
                navArgument("horario") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 0
            val horario = backStackEntry.arguments?.getString("horario") ?: ""
            val clase = clasesDeEjemplo.first { it.id == claseId }

            ConfirmacionScreen(
                clase = clase,
                horarioElegido = horario,
                onVerReservasClick = {
                    navController.navigate(Pantalla.Reservas.ruta) {
                        popUpTo(Pantalla.Inicio.ruta)
                    }
                }
            )
        }

        composable(Pantalla.Reservas.ruta) {
            Text("Pantalla de Reservas (pendiente)")
        }

        composable(Pantalla.Rutinas.ruta) {
            RutinasScreen()
        }

        composable(Pantalla.Perfil.ruta) {
            Text("Pantalla de Perfil (pendiente)")
        }
    }
}