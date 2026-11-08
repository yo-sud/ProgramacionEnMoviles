package com.tecsup.pokemon.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.ui.AppViewModelFactory
import com.tecsup.pokemon.ui.admin.AdminScreen
import com.tecsup.pokemon.ui.admin.AdminViewModel
import com.tecsup.pokemon.ui.detalle.DetalleScreen
import com.tecsup.pokemon.ui.detalle.DetalleViewModel
import com.tecsup.pokemon.ui.favoritos.FavoritosScreen
import com.tecsup.pokemon.ui.favoritos.FavoritosViewModel
import com.tecsup.pokemon.ui.lista.ListaScreen
import com.tecsup.pokemon.ui.lista.ListaViewModel
import com.tecsup.pokemon.ui.login.LoginScreen
import com.tecsup.pokemon.ui.login.LoginViewModel

@Composable
fun AppNav(container: AppContainer) {
    val factory = remember { AppViewModelFactory(container) }
    val nav = rememberNavController()
    val loginVm: LoginViewModel = viewModel(factory = factory)
    val sesion by loginVm.sesion.collectAsStateWithLifecycle()

    NavHost(nav, startDestination = "login") {
        composable("login") {
            LoginScreen(loginVm) {
                nav.navigate("lista") { popUpTo("login") { inclusive = true } }
            }
        }
        composable("lista") {
            val vm: ListaViewModel = viewModel(factory = factory)
            ListaScreen(
                vm = vm,
                esAdmin = sesion?.rol == Rol.ADMIN,
                onItem = { nav.navigate("detalle/$it") },
                onFavoritos = { nav.navigate("favoritos") },
                onAdmin = { nav.navigate("admin") },
                onSalir = {
                    loginVm.cerrarSesion()
                    nav.navigate("login") { popUpTo(0) }
                }
            )
        }
        composable("detalle/{id}", arguments = listOf(navArgument("id") { type = NavType.StringType })) { e ->
            val vm: DetalleViewModel = viewModel(factory = factory)
            DetalleScreen(vm, e.arguments?.getString("id").orEmpty()) { nav.popBackStack() }
        }
        composable("favoritos") {
            val vm: FavoritosViewModel = viewModel(factory = factory)
            FavoritosScreen(vm, { nav.navigate("detalle/$it") }) { nav.popBackStack() }
        }
        composable("admin") {
            if (sesion?.rol != Rol.ADMIN) {
                LaunchedEffect(Unit) { nav.popBackStack() }
            } else {
                val vm: AdminViewModel = viewModel(factory = factory)
                AdminScreen(vm) { nav.popBackStack() }
            }
        }
    }
}