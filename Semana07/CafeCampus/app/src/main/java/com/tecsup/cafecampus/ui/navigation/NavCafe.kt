package com.tecsup.cafecampus.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.cafecampus.CafeApp
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.admin.AjustesScreen
import com.tecsup.cafecampus.ui.admin.DetallePedidoAdminScreen
import com.tecsup.cafecampus.ui.admin.FormularioProductoScreen
import com.tecsup.cafecampus.ui.admin.MenuAdminScreen
import com.tecsup.cafecampus.ui.admin.PanelScreen
import com.tecsup.cafecampus.ui.admin.PedidosAdminScreen
import com.tecsup.cafecampus.ui.cliente.ConfirmarPedidoScreen
import com.tecsup.cafecampus.ui.cliente.DetalleProductoScreen
import com.tecsup.cafecampus.ui.cliente.LoginScreen
import com.tecsup.cafecampus.ui.cliente.MenuScreen
import com.tecsup.cafecampus.ui.cliente.MisPedidosScreen
import com.tecsup.cafecampus.ui.cliente.SeguimientoScreen
import com.tecsup.cafecampus.ui.cliente.SplashScreen
import com.tecsup.cafecampus.ui.primerNombre
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.CafeViewModelFactory
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel
import kotlinx.coroutines.delay

private data class ItemTab(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector,
    val iconoActivo: ImageVector = icono
)

/**
 * Grafo de navegación (archivo GUIADO). Los ViewModels se crean UNA vez aquí
 * (alcance de la Activity) y se pasan a cada pantalla; así el carrito y los
 * filtros sobreviven a rotaciones y a la navegación.
 */
@Composable
fun NavCafe() {
    val app = LocalContext.current.applicationContext as CafeApp
    val contexto = LocalContext.current
    val factory = remember { CafeViewModelFactory(app) }
    val nav = rememberNavController()

    val menuVm: MenuViewModel = viewModel(factory = factory)
    val sesionVm: SesionViewModel = viewModel(factory = factory)
    val pedidosClienteVm: PedidosClienteViewModel = viewModel(factory = factory)
    val panelVm: PanelAdminViewModel = viewModel(factory = factory)
    val pedidosAdminVm: PedidosAdminViewModel = viewModel(factory = factory)
    val menuAdminVm: MenuAdminViewModel = viewModel(factory = factory)

    val usuario by sesionVm.usuarioActual.collectAsStateWithLifecycle()
    val oscuro by sesionVm.modoOscuro.collectAsStateWithLifecycle()
    val rol = usuario?.let { Rol.valueOf(it.rol) } ?: Rol.CLIENTE

    // El ViewModel de pedidos necesita saber quién es el cliente actual
    LaunchedEffect(usuario?.id) { usuario?.let { pedidosClienteVm.setCliente(it.id) } }

    // Repetir pedido: el ViewModel avisa el resultado; aquí se informa y se navega al carrito
    LaunchedEffect(Unit) {
        menuVm.repeticion.collect { r ->
            val texto = when {
                r.agregados == 0 -> "Ninguno de esos productos está disponible ahora"
                r.omitidos > 0 -> "Se agregaron ${r.agregados} producto(s). ${r.omitidos} ya no está(n) disponible(s)"
                else -> "Productos agregados a tu bolsa"
            }
            Toast.makeText(contexto, texto, Toast.LENGTH_LONG).show()
            if (r.agregados > 0) nav.navigate(Rutas.CONFIRMAR)
        }
    }

    // ---- Barra inferior: solo se muestra en las pestañas principales ----
    val tabsCliente = listOf(
        ItemTab(Rutas.MENU, "Menú", Icons.Outlined.Home, Icons.Filled.Home),
        ItemTab(Rutas.MIS_PEDIDOS, "Pedidos", Icons.AutoMirrored.Outlined.FormatListBulleted),
        ItemTab(Rutas.PERFIL, "Perfil", Icons.Outlined.Person, Icons.Filled.Person)
    )
    val tabsAdmin = listOf(
        ItemTab(Rutas.PANEL, "Panel", Icons.Outlined.BarChart, Icons.Filled.BarChart),
        ItemTab(Rutas.PEDIDOS_ADMIN, "Pedidos", Icons.AutoMirrored.Outlined.FormatListBulleted),
        ItemTab(Rutas.MENU_ADMIN, "Menú", Icons.Outlined.LocalCafe, Icons.Filled.LocalCafe),
        ItemTab(Rutas.AJUSTES, "Ajustes", Icons.Outlined.Settings, Icons.Filled.Settings)
    )
    val entrada by nav.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route
    val tabs = when {
        // CAMBIO 1: el Seguimiento también muestra la barra del estudiante (como en el diseño)
        tabsCliente.any { it.ruta == ruta } || ruta == Rutas.SEGUIMIENTO -> tabsCliente
        tabsAdmin.any { it.ruta == ruta } -> tabsAdmin
        else -> emptyList()
    }

    fun irATab(destino: String, inicio: String) {
        nav.navigate(destino) {
            popUpTo(inicio) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun grafoDe(r: Rol) = if (r == Rol.ADMIN) Rutas.GRAFO_ADMIN else Rutas.GRAFO_CLIENTE

    fun salir() {
        menuVm.vaciar() // el carrito no debe pasar de una cuenta a otra
        sesionVm.cerrarSesion {
            nav.navigate(Rutas.LOGIN) { popUpTo(nav.graph.id) { inclusive = true } }
        }
    }

    /** Cambio de perfil: va al grafo del otro perfil y limpia TODA la pila de navegación. */
    fun cambiarDePerfil(destino: Rol) {
        menuVm.vaciar()
        nav.navigate(grafoDe(destino)) { popUpTo(nav.graph.id) { inclusive = true } }
    }

    /** Desde la bienvenida: va al Login sin borrarla de la pila (Atrás vuelve a la bienvenida). */
    fun irALogin() { nav.navigate(Rutas.LOGIN) }

    CafeTheme(rol = rol, oscuro = oscuro) {
        Scaffold(
            bottomBar = {
                if (tabs.size > 1) {
                    Column {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            tonalElevation = 0.dp
                        ) {
                            tabs.forEach { t ->
                                // CAMBIO 2: dentro del Seguimiento, la pestaña "Pedidos" aparece activa
                                val activa = ruta == t.ruta ||
                                        (ruta == Rutas.SEGUIMIENTO && t.ruta == Rutas.MIS_PEDIDOS)
                                NavigationBarItem(
                                    selected = activa,
                                    onClick = { irATab(t.ruta, tabs.first().ruta) },
                                    icon = {
                                        Icon(if (activa) t.iconoActivo else t.icono, contentDescription = t.titulo)
                                    },
                                    label = { Text(t.titulo) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = Color.Transparent,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = nav,
                startDestination = Rutas.SPLASH,
                modifier = Modifier.padding(padding)
            ) {
                composable(Rutas.SPLASH) {
                    // Los botones aparecen solo si NO hay sesión guardada
                    var mostrarAcciones by rememberSaveable { mutableStateOf(false) }
                    SplashScreen(
                        mostrarAcciones = mostrarAcciones,
                        onComenzar = { irALogin() },
                        onTengoCuenta = { irALogin() }
                    )
                    LaunchedEffect(Unit) {
                        if (mostrarAcciones) return@LaunchedEffect
                        delay(900)
                        // Si ya hay perfil guardado salta directo a su pantalla principal
                        val destino = when (sesionVm.restaurar()) {
                            Rol.ADMIN -> Rutas.GRAFO_ADMIN
                            Rol.CLIENTE -> Rutas.GRAFO_CLIENTE
                            null -> null
                        }
                        if (destino == null) mostrarAcciones = true
                        else nav.navigate(destino) { popUpTo(Rutas.SPLASH) { inclusive = true } }
                    }
                }

                composable(Rutas.LOGIN) {
                    LoginScreen(
                        vm = sesionVm,
                        onIngreso = { r ->
                            nav.navigate(grafoDe(r)) { popUpTo(nav.graph.id) { inclusive = true } }
                        },
                        onOlvide = {
                            Toast.makeText(contexto, "Recuperar contraseña estará disponible próximamente", Toast.LENGTH_SHORT).show()
                        },
                        onRegistro = {
                            Toast.makeText(contexto, "El registro estará disponible próximamente", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // ---------- Perfil Estudiante ----------
                navigation(startDestination = Rutas.MENU, route = Rutas.GRAFO_CLIENTE) {
                    composable(Rutas.MENU) {
                        MenuScreen(
                            vm = menuVm,
                            nombre = usuario?.nombre?.primerNombre().orEmpty(),
                            onProducto = { nav.navigate(Rutas.producto(it)) },
                            onBolsa = { nav.navigate(Rutas.CONFIRMAR) }
                        )
                    }
                    composable(
                        route = Rutas.PRODUCTO,
                        arguments = listOf(navArgument(Rutas.ARG_ID) { type = NavType.IntType })
                    ) { entradaNav ->
                        DetalleProductoScreen(
                            vm = menuVm,
                            productoId = entradaNav.arguments?.getInt(Rutas.ARG_ID) ?: 0,
                            onVolver = { nav.popBackStack() }
                        )
                    }
                    composable(Rutas.CONFIRMAR) {
                        ConfirmarPedidoScreen(
                            menuVm = menuVm,
                            pedidosVm = pedidosClienteVm,
                            sesionVm = sesionVm,
                            onSeguirPidiendo = { nav.popBackStack(Rutas.MENU, inclusive = false) },
                            onPedidoCreado = { id ->
                                // Al volver atrás desde el seguimiento se llega al Menú, no a Confirmar
                                nav.navigate(Rutas.seguimiento(id)) { popUpTo(Rutas.MENU) }
                            },
                            onVolver = { nav.popBackStack() }
                        )
                    }
                    composable(
                        route = Rutas.SEGUIMIENTO,
                        arguments = listOf(navArgument(Rutas.ARG_ID) { type = NavType.IntType })
                    ) { entradaNav ->
                        SeguimientoScreen(
                            vm = pedidosClienteVm,
                            pedidoId = entradaNav.arguments?.getInt(Rutas.ARG_ID) ?: 0,
                            onVolver = { nav.popBackStack() }
                        )
                    }
                    composable(Rutas.MIS_PEDIDOS) {
                        MisPedidosScreen(
                            vm = pedidosClienteVm,
                            onPedido = { nav.navigate(Rutas.seguimiento(it)) },
                            onRepetir = { menuVm.repetirPedido(it.detalles) },
                            onVolver = { irATab(Rutas.MENU, Rutas.MENU) }
                        )
                    }
                    composable(Rutas.PERFIL) {
                        AjustesScreen(
                            vm = sesionVm,
                            onCambioPerfil = { cambiarDePerfil(it) },
                            onCerrarSesion = { salir() }
                        )
                    }
                }

                // ---------- Perfil Administrador ----------
                navigation(startDestination = Rutas.PANEL, route = Rutas.GRAFO_ADMIN) {
                    composable(Rutas.PANEL) {
                        PanelScreen(
                            vm = panelVm,
                            onVerPedidos = {
                                pedidosAdminVm.seleccionar(EstadoPedido.RECIBIDO) // "Ver todos" y la campana muestran los Nuevos
                                irATab(Rutas.PEDIDOS_ADMIN, Rutas.PANEL)
                            }
                        )
                    }
                    composable(Rutas.PEDIDOS_ADMIN) {
                        PedidosAdminScreen(
                            vm = pedidosAdminVm,
                            onDetalle = { nav.navigate(Rutas.pedidoAdmin(it)) },
                            onVolver = { irATab(Rutas.PANEL, Rutas.PANEL) }
                        )
                    }
                    composable(
                        route = Rutas.PEDIDO_ADMIN,
                        arguments = listOf(navArgument(Rutas.ARG_ID) { type = NavType.IntType })
                    ) { entradaNav ->
                        DetallePedidoAdminScreen(
                            vm = pedidosAdminVm,
                            pedidoId = entradaNav.arguments?.getInt(Rutas.ARG_ID) ?: 0,
                            onVolver = { nav.popBackStack() }
                        )
                    }
                    composable(Rutas.MENU_ADMIN) {
                        MenuAdminScreen(
                            vm = menuAdminVm,
                            onNuevo = { nav.navigate(Rutas.formulario(0)) },
                            onEditar = { nav.navigate(Rutas.formulario(it)) }
                        )
                    }
                    composable(
                        route = Rutas.FORMULARIO,
                        arguments = listOf(navArgument(Rutas.ARG_ID) { type = NavType.IntType })
                    ) { entradaNav ->
                        FormularioProductoScreen(
                            vm = menuAdminVm,
                            productoId = entradaNav.arguments?.getInt(Rutas.ARG_ID) ?: 0,
                            onListo = { nav.popBackStack() }
                        )
                    }
                    composable(Rutas.AJUSTES) {
                        AjustesScreen(
                            vm = sesionVm,
                            onCambioPerfil = { cambiarDePerfil(it) },
                            onCerrarSesion = { salir() }
                        )
                    }
                }
            }
        }
    }
}