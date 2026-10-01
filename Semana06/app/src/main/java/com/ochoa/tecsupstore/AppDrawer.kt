package com.ochoa.tecsupstore

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    rutaActual: String,
    onNavegar: (String) -> Unit
) {
    ModalDrawerSheet {
        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = false,
            onClick = { onNavegar(Rutas.INICIO) },
            icon = { Icon(Icons.Default.Home, contentDescription = null) })
        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            selected = false,
            onClick = { onNavegar(Rutas.PEDIDOS) },
            icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) })
        NavigationDrawerItem(
            label = { Text("Favoritos") },
            selected = false,
            onClick = { onNavegar(Rutas.FAVORITOS) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) }
        )
        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = false,
            onClick = { onNavegar(Rutas.PERFIL) },
            icon = { Icon(Icons.Default.Person, contentDescription = null) })
    }
}
