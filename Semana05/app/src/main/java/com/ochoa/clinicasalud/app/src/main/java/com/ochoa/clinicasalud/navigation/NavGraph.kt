package com.ochoa.clinicasalud.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ochoa.clinicasalud.model.medicosDeEjemplo
import com.ochoa.clinicasalud.ui.screens.AgendarCitaScreen
import com.ochoa.clinicasalud.ui.screens.ConfirmacionScreen
import com.ochoa.clinicasalud.ui.screens.InicioScreen
import com.ochoa.clinicasalud.ui.screens.PerfilMedicoScreen

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
                onMedicoClick = { medico ->
                    navController.navigate(Pantalla.PerfilMedico.crearRuta(medico.id))
                }
            )
        }

        composable(
            route = Pantalla.PerfilMedico.ruta,
            arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
            val medico = medicosDeEjemplo.first { it.id == medicoId }

            PerfilMedicoScreen(
                medico = medico,
                onAgendarCitaClick = {
                    navController.navigate(Pantalla.AgendarCita.crearRuta(medico.id))
                }
            )
        }

        composable(
            route = Pantalla.AgendarCita.ruta,
            arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
            val medico = medicosDeEjemplo.first { it.id == medicoId }

            AgendarCitaScreen(
                medico = medico,
                onConfirmarClick = { fecha, hora ->
                    navController.navigate(
                        Pantalla.Confirmacion.crearRuta(medico.id, fecha, hora)
                    )
                }
            )
        }

        composable(
            route = Pantalla.Confirmacion.ruta,
            arguments = listOf(
                navArgument("medicoId") { type = NavType.IntType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            val medico = medicosDeEjemplo.first { it.id == medicoId }

            ConfirmacionScreen(
                medico = medico,
                fecha = fecha,
                hora = hora,
                onVolverInicioClick = {
                    navController.navigate(Pantalla.Inicio.ruta) {
                        popUpTo(Pantalla.Inicio.ruta) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Pantalla.MisCitas.ruta) {
            androidx.compose.material3.Text("Mis citas (pendiente)")
        }

        composable(Pantalla.HistorialMedico.ruta) {
            androidx.compose.material3.Text("Historial médico (pendiente)")
        }
    }
}