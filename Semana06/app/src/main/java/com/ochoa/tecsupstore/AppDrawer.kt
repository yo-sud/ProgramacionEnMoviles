package com.ochoa.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    rutaActual: String,
    cantidadFavoritos: Int,
    onNavegar: (String) -> Unit
) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text("JO", color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(Modifier.height(8.dp))
            Text("Jose Ochoa", style = MaterialTheme.typography.titleMedium)
            Text("jose.ochoa@tecsup.edu.pe", style = MaterialTheme.typography.bodyMedium)
        }
        HorizontalDivider()
        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = rutaActual == Rutas.INICIO,
            onClick = { onNavegar(Rutas.INICIO) },
            icon = { Icon(Icons.Default.Home, contentDescription = null) }
        )
        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            selected = rutaActual == Rutas.PEDIDOS,
            onClick = { onNavegar(Rutas.PEDIDOS) },
            icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) }
        )
        NavigationDrawerItem(
            label = { Text("Favoritos") },
            selected = rutaActual == Rutas.FAVORITOS,
            onClick = { onNavegar(Rutas.FAVORITOS) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            badge = {
                if (cantidadFavoritos > 0) {
                    Badge { Text(cantidadFavoritos.toString()) }
                }
            }
        )
        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = rutaActual == Rutas.PERFIL,
            onClick = { onNavegar(Rutas.PERFIL) },
            icon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
    }
}