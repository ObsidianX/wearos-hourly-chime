package net.obsidianx.watchchime

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.Calendar

object ChimeScheduler {

    private const val REQUEST_CODE = 1001

    fun scheduleNext(context: Context, test: Boolean = false) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val pendingIntent = buildPendingIntent(context, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextHour = Calendar.getInstance().apply {
            if (test) {
                add(Calendar.SECOND, 10)
            } else {
                add(Calendar.HOUR_OF_DAY, 1)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            set(Calendar.MILLISECOND, 0)
        }

        Log.i("Chime", "Scheduling next chime for: ${nextHour.time}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            alarmManager.cancelAll()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, nextHour.timeInMillis, pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, nextHour.timeInMillis, pendingIntent
            )
        }
    }

    fun cancel(context: Context) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        buildPendingIntent(context, PendingIntent.FLAG_NO_CREATE).let {
            alarmManager.cancel(it)
        }
    }

    fun needsExactAlarmPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return false
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        return !alarmManager.canScheduleExactAlarms()
    }

    private fun buildPendingIntent(context: Context, flags: Int): PendingIntent {
        val intent = Intent(context, ChimeReceiver::class.java)
        return PendingIntent.getBroadcast(
            context, REQUEST_CODE, intent,
            flags or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
