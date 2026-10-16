package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.ProductoCard
import com.tecsup.cafecampus.viewmodel.MenuViewModel

/**
 * EJEMPLO RESUELTO. La View solo observa el ViewModel y le avisa de los clics:
 * no usa DAO, Repository ni lógica de negocio.
 */
@Composable
fun MenuScreen(
    vm: MenuViewModel,
    onProducto: (Int) -> Unit,
    onBolsa: () -> Unit = {},
    onSalir: () -> Unit = {}
) {
    val productos by vm.productosVisibles.collectAsStateWithLifecycle()
    val categorias by vm.categorias.collectAsStateWithLifecycle()
    val categoria by vm.categoria.collectAsStateWithLifecycle()
    val cantidad by vm.cantidadItems.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        AppTopBar("CafeCampus", acciones = {
            IconButton(onClick = onBolsa) {
                BadgedBox(badge = { if (cantidad > 0) Badge { Text("$cantidad") } }) {
                    Icon(Icons.Filled.ShoppingBag, contentDescription = "Bolsa")
                }
            }
            IconButton(onClick = onSalir) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
            }
        })

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { FilterChip(selected = categoria == null, onClick = { vm.elegirCategoria(null) }, label = { Text("Todos") }) }
            items(categorias) { c ->
                FilterChip(selected = categoria == c, onClick = { vm.elegirCategoria(c) }, label = { Text(c) })
            }
        }

        if (productos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No hay productos") }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productos, key = { it.id }) { p -> ProductoCard(p) { onProducto(p.id) } }
            }
        }
    }
}