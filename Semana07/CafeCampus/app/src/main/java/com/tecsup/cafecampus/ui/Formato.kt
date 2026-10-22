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