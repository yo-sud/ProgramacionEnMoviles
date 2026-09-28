package com.ochoa.clinicasalud.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ochoa.clinicasalud.model.medicosDeEjemplo
import com.ochoa.clinicasalud.ui.screens.InicioScreen

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
            Text("Perfil de: ${medico.nombre}")
        }
    }
}