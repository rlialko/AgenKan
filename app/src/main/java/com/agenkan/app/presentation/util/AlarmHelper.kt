package com.agenkan.app.presentation.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.agenkan.app.presentation.receiver.AlarmReceiver
import java.util.*

class AlarmHelper {
    companion object {

        fun updatedAlarm(context: Context, isBlocked: Boolean) {

            val myIntent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0)

            val alarmManager =
                context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.SECOND, 2) // first time
            val frequency = (5 * 1000).toLong()
            if (isBlocked) {
                Log.i("AGEN", "start")
                alarmManager.cancel(pendingIntent)
                alarmManager.setRepeating(
                    AlarmManager.RTC,
                    calendar.timeInMillis,
                    frequency,
                    pendingIntent
                )
            } else {
                alarmManager.cancel(pendingIntent)
                Log.i("AGEN", "cancel")
            }
        }

    }
}