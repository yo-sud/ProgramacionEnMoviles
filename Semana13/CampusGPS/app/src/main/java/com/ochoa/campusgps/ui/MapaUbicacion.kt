package com.ochoa.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.ochoa.campusgps.model.LugarConDistancia
import com.ochoa.campusgps.model.UbicacionUi
import androidx.compose.runtime.key
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.ochoa.campusgps.model.Lugar

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(estado.posicion.latitud, estado.posicion.longitud), 16f
                )
            )
        }
    }

    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
        if (lugar != null) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(lugar.latitud, lugar.longitud), 17f)
            )
        }
    }

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        if (estado is UbicacionUi.Lista) {
            key(estado.posicion) {
                Marker(
                    state = rememberMarkerState(
                        position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                    ),
                    title = "Estás aquí"
                )
            }
        }
        lugares.forEach { item ->
            key(item.lugar.id) {
                val markerState = rememberMarkerState(
                    position = LatLng(item.lugar.latitud, item.lugar.longitud)
                )
                LaunchedEffect(seleccionado) {
                    if (seleccionado == item.lugar.id) markerState.showInfoWindow()
                }
                Marker(
                    state = markerState,
                    title = item.lugar.nombre,
                    snippet = item.lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (item.lugar.id == masCercano?.id) BitmapDescriptorFactory.HUE_GREEN
                        else BitmapDescriptorFactory.HUE_AZURE
                    )
               )
            }
        }
    }
}