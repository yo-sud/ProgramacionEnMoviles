package com.tecsup.cafecampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"

    const val GRAFO_CLIENTE = "grafo_cliente"
    const val MENU = "menu"
    const val ARG_ID = "id"
    const val PRODUCTO = "producto/{$ARG_ID}"
    fun producto(id: Int) = "producto/$id"

    const val CONFIRMAR = "confirmar"
    const val SEGUIMIENTO = "seguimiento/{$ARG_ID}"
    fun seguimiento(id: Int) = "seguimiento/$id"

    const val MIS_PEDIDOS = "mis_pedidos"
    const val PERFIL = "perfil"

    const val GRAFO_ADMIN = "grafo_admin"
    const val PANEL = "panel"
    const val PEDIDOS_ADMIN = "pedidos_admin"
    const val PEDIDO_ADMIN = "pedido_admin/{$ARG_ID}"
    fun pedidoAdmin(id: Int) = "pedido_admin/$id"

    const val MENU_ADMIN = "menu_admin"
    const val FORMULARIO = "formulario/{$ARG_ID}"
    fun formulario(id: Int) = "formulario/$id"

    const val AJUSTES = "ajustes"
}
