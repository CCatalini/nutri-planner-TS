package com.austral.nutri_planner_ts.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.austral.nutri_planner_ts.MainActivity
import com.austral.nutri_planner_ts.R
import kotlin.random.Random

const val NUTRIPLANNER_NOTIFICATION_CHANNEL_ID = "nutriplanner_notification_channel"

/**
 * BroadcastReceiver encargado de generar y mostrar la notificación una vez que el
 * [android.app.AlarmManager] dispara la alarma.
 */
class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Recuperamos los datos enviados en el intent (título, mensaje e id).
        val title = intent.getStringExtra(KEY_TITLE) ?: context.getString(R.string.app_name)
        val message = intent.getStringExtra(KEY_MESSAGE)
            ?: context.getString(R.string.notification_default_message)
        val notificationId = intent.getIntExtra(KEY_NOTIFICATION_ID, Random.nextInt())

        // Intent que abrirá la aplicación cuando el usuario pulse la notificación.
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // A partir de Android O es obligatorio crear un canal de notificaciones.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NUTRIPLANNER_NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.notification_channel_description)
                enableLights(true)
                lightColor = Color.MAGENTA
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, NUTRIPLANNER_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher) // Usa tu propio icono si lo prefieres
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE = "message"
        const val KEY_NOTIFICATION_ID = "notification_id"
    }
} 