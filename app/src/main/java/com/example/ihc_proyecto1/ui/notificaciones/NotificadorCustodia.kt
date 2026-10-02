package com.example.ihc_proyecto1.ui.notificaciones

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.ihc_proyecto1.MainActivity
import com.example.ihc_proyecto1.R

/**
 * Avisos del sistema sobre el deslinde del objeto.
 * Usa un solo canal: "Deslinde de objetos".
 */
object NotificadorCustodia {

    private const val CANAL = "deslinde_custodia"
    private const val ID_DESLINDE = 1001

    /** Crea el canal. Se llama al arrancar la app para que exista a tiempo. */
    fun crearCanal(contexto: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val canal = NotificationChannel(
            CANAL,
            "Deslinde de objetos",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Avisos cuando tu objeto fue entregado en vigilancia o secretaría."
        }
        contexto.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    /** En Android 13+ hace falta el permiso POST_NOTIFICATIONS para notificar. */
    fun tienePermiso(contexto: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(contexto, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

    /** "Deslinde confirmado": el objeto ya fue recibido por el destino. */
    @SuppressLint("MissingPermission")
    fun mostrarDeslindeConfirmado(contexto: Context, destino: String) {
        if (!tienePermiso(contexto)) return

        val abrirApp = PendingIntent.getActivity(
            contexto,
            0,
            Intent(contexto, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val texto = "Tu objeto fue recibido por $destino"

        val notificacion = NotificationCompat.Builder(contexto, CANAL)
            .setSmallIcon(R.drawable.ic_deslinde)
            .setContentTitle("Deslinde confirmado")
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(texto))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setAutoCancel(true)
            .setContentIntent(abrirApp)
            .build()

        NotificationManagerCompat.from(contexto).notify(ID_DESLINDE, notificacion)
    }
}