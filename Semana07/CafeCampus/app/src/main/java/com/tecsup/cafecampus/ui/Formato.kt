package com.tecsup.cafecampus.ui

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

/** 12.5 -> "S/ 12.50" */
fun Double.soles(): String = "S/ " + String.format(Locale.US, "%.2f", this)

/** Millis -> "14/10/2026 12:30" */
fun Long.fechaHora(): String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(this))

/** Millis -> "Hoy 10:30", "Ayer 08:40" o "14/10/2026 16:10" para fechas más antiguas. */
fun Long.fechaRelativa(): String {
    val zona = ZoneId.systemDefault()
    val momento = Instant.ofEpochMilli(this).atZone(zona)
    val hoy = LocalDate.now(zona)
    val hora = momento.format(DateTimeFormatter.ofPattern("HH:mm"))
    return when (momento.toLocalDate()) {
        hoy -> "Hoy $hora"
        hoy.minusDays(1) -> "Ayer $hora"
        else -> momento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    }
}

/** "CAJA" -> "Caja", "YAPE" -> "Yape / Plin" */
fun String.etiquetaPago(): String = if (this == "YAPE") "Yape / Plin" else "Caja"

/** "Juan Pérez" -> "Juan" */
fun String.primerNombre(): String = trim().substringBefore(' ')

/** "Ana Torres Ruiz" -> "Ana T."; "Juan" -> "Juan" */
fun String.nombreCorto(): String {
    val partes = trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    return when {
        partes.isEmpty() -> ""
        partes.size == 1 -> partes[0]
        else -> "${partes[0]} ${partes[1].first().uppercaseChar()}."
    }
}

/** 46 -> "#0046" */
fun Int.numeroPedido(): String = "#" + String.format(Locale.US, "%04d", this)

/** "10:30" -> "Recojo a las 10:30"; "Ahora" o "Lo antes posible" -> "Recojo lo antes posible" */
fun String.textoRecojo(): String =
    if (isBlank() || this == "Ahora" || this == "Lo antes posible") "Recojo lo antes posible" else "Recojo a las $this"

/** "10:45" -> "10:45"; "Ahora" o "Lo antes posible" -> "Ahora" */
fun String.horaCorta(): String = if (isBlank() || this == "Lo antes posible") "Ahora" else this

/** "10:45" -> "Recojo 10:45"; "Ahora" o "Lo antes posible" -> "Recojo ahora" */
fun String.recojoCorto(): String =
    if (isBlank() || this == "Ahora" || this == "Lo antes posible") "Recojo ahora" else "Recojo $this"