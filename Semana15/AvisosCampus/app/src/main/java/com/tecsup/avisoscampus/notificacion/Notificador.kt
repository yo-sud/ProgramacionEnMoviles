package com.tecsup.avisoscampus.notificacion

import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import com.tecsup.avisoscampus.model.Aviso
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.Manifest
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tecsup.avisoscampus.R

// [G] canal, permiso y mostrar
object Notificador {
    const val CANAL = "avisos"

    // Paso 1
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT)
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(canal)
        }
    }

    // Paso 2
    fun tienePermiso(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    // Pasos 3 y 4
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false

        val intent = Intent(context, com.tecsup.avisoscampus.MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            aviso.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
        return true
    }
}
