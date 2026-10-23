package com.tecsup.cafecampus.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** 12.5 -> "S/ 12.50" */
fun Double.soles(): String = "S/ " + String.format(Locale.US, "%.2f", this)

/** Millis -> "14/10/2026 12:30" */
fun Long.fechaHora(): String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(this))

/** "CAJA" -> "Caja", "YAPE" -> "Yape / Plin" */
fun String.etiquetaPago(): String = if (this == "YAPE") "Yape / Plin" else "Caja"

/** "Juan Pérez" -> "Juan" */
fun String.primerNombre(): String = trim().substringBefore(' ')

/** 46 -> "#0046" */
fun Int.numeroPedido(): String = "#" + String.format(Locale.US, "%04d", this)

/** "10:30" -> "Recojo a las 10:30"; "Ahora" o "Lo antes posible" -> "Recojo lo antes posible" */
fun String.textoRecojo(): String =
    if (isBlank() || this == "Ahora" || this == "Lo antes posible") "Recojo lo antes posible" else "Recojo a las $this"