package com.tecsup.cafecampus.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.cafecampus.CafeApp
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.cliente.DetalleProductoScreen
import com.tecsup.cafecampus.ui.cliente.LoginScreen
import com.tecsup.cafecampus.ui.cliente.MenuScreen
import com.tecsup.cafecampus.ui.cliente.SplashScreen
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.CafeViewModelFactory
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel
import kotlinx.coroutines.delay

/**
 * Grafo de navegación (archivo GUIADO). Los ViewModels se crean UNA vez aquí
 * (alcance de la Activity) y se pasan a cada pantalla; así el carrito y los
 * filtros sobreviven a rotaciones y a la navegación.
 */
@Composable
fun NavCafe() {
    val app = LocalContext.current.applicationContext as CafeApp
    val factory = remember { CafeViewModelFactory(app) }
    val nav = rememberNavController()

    val menuVm: MenuViewModel = viewModel(factory = factory)
    val sesionVm: SesionViewModel = viewModel(factory = factory)
    // TODO (Paso 8): PedidosClienteViewModel · (Paso 9): PanelAdminViewModel y PedidosAdminViewModel
    // TODO (Paso 10): MenuAdminViewModel

    val usuario by sesionVm.usuarioActual.collectAsStateWithLifecycle()
    val oscuro by sesionVm.modoOscuro.collectAsStateWithLifecycle()
    val rol = usuario?.let { Rol.valueOf(it.rol) } ?: Rol.CLIENTE

    fun grafoDe(r: Rol) = if (r == Rol.ADMIN) Rutas.GRAFO_ADMIN else Rutas.GRAFO_CLIENTE

    fun salir() {
        menuVm.vaciar()
        sesionVm.cerrarSesion {
            nav.navigate(Rutas.LOGIN) { popUpTo(nav.graph.id) { inclusive = true } }
        }
    }

    CafeTheme(rol = rol, oscuro = oscuro) {
        // TODO (Pasos 8-11): bottomBar con NavigationBar (3 ítems cliente, 4 admin)
        Scaffold { padding ->
            NavHost(
                navController = nav,
                startDestination = Rutas.SPLASH,
                modifier = Modifier.padding(padding)
            ) {
                composable(Rutas.SPLASH) {
                    SplashScreen()
                    LaunchedEffect(Unit) {
                        delay(900)
                        // Si ya hay perfil guardado salta el login y va directo a su pantalla principal
                        val destino = when (sesionVm.restaurar()) {
                            Rol.ADMIN -> Rutas.GRAFO_ADMIN
                            Rol.CLIENTE -> Rutas.GRAFO_CLIENTE
                            null -> Rutas.LOGIN
                        }
                        nav.navigate(destino) { popUpTo(Rutas.SPLASH) { inclusive = true } }
                    }
                }

                composable(Rutas.LOGIN) {
                    LoginScreen(vm = sesionVm, onIngreso = { r ->
                        nav.navigate(grafoDe(r)) { popUpTo(Rutas.LOGIN) { inclusive = true } }
                    })
                }

                navigation(startDestination = Rutas.MENU, route = Rutas.GRAFO_CLIENTE) {
                    composable(Rutas.MENU) {
                        MenuScreen(
                            vm = menuVm,
                            onProducto = { nav.navigate(Rutas.producto(it)) },
                            // TODO (Paso 8): onBolsa = { nav.navigate(Rutas.CONFIRMAR) },
                            onSalir = { salir() }
                        )
                    }
                    composable(
                        route = Rutas.PRODUCTO,
                        arguments = listOf(navArgument(Rutas.ARG_ID) { type = NavType.IntType })
                    ) { entrada ->
                        DetalleProductoScreen(
                            vm = menuVm,
                            productoId = entrada.arguments?.getInt(Rutas.ARG_ID) ?: 0,
                            onVolver = { nav.popBackStack() }
                        )
                    }
                    // TODO (Paso 8): CONFIRMAR, SEGUIMIENTO, MIS_PEDIDOS · (Paso 11): PERFIL
                }

                // Pantalla provisional: el Panel real se construye en el Paso 9
                navigation(startDestination = Rutas.PANEL, route = Rutas.GRAFO_ADMIN) {
                    composable(Rutas.PANEL) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Panel del administrador (se construye en el Paso 9)")
                            Spacer(Modifier.height(16.dp))
                            CafeButton("Cerrar sesión", onClick = { salir() })
                        }
                    }
                }
            }
        }
    }
}