package com.ochoa.campusgps.data

import com.ochoa.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
}
