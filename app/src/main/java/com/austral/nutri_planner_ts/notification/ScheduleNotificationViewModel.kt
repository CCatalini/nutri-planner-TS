package com.austral.nutri_planner_ts.notification

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ScheduleNotificationViewModel @Inject constructor(
    private val app: Application
) : ViewModel() {

    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Programa una notificación única que se disparará en [delayMillis] milisegundos.
     * Por defecto la programamos a los 3 s, imitando el ejemplo original.
     */
    fun scheduleOneTimeNotification(delayMillis: Long = 3_000) {
        viewModelScope.launch(Dispatchers.IO) {
            val triggerAt = System.currentTimeMillis() + delayMillis
            val pendingIntent = buildPendingIntent(
                requestCode = ONE_TIME_REQUEST_CODE,
                title = "¡Hora de comer!",
                message = "Revisa tu plan nutricional",
            )
            // Alarm exacta incluso con Doze.
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        }
    }

    /**
     * Programa una notificación diaria que se lanzará a la hora y minuto indicados.
     * Por defecto: 20:00 (8 p. m.).
     */
    fun scheduleDailyNotification(hourOfDay: Int = 20, minute: Int = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (timeInMillis <= System.currentTimeMillis()) {
                    // Si la hora ya pasó hoy, márcala para mañana.
                    add(Calendar.DATE, 1)
                }
            }

            val pendingIntent = buildPendingIntent(
                requestCode = DAILY_REQUEST_CODE,
                title = "Tu recordatorio diario",
                message = "No olvides registrar tu ingesta de hoy",
            )

            // Repetimos cada día exacto.
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    private fun buildPendingIntent(
        requestCode: Int,
        title: String,
        message: String,
    ): PendingIntent {
        val intent = Intent(app, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.KEY_TITLE, title)
            putExtra(NotificationReceiver.KEY_MESSAGE, message)
            putExtra(NotificationReceiver.KEY_NOTIFICATION_ID, requestCode)
        }
        return PendingIntent.getBroadcast(
            app,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    companion object {
        private const val ONE_TIME_REQUEST_CODE = 1001
        private const val DAILY_REQUEST_CODE = 1002
    }
} 