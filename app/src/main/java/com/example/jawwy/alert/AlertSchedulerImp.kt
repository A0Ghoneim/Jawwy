package com.example.jawwy.alert

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.LocalDateTime
import java.time.ZoneId

class AlertSchedulerImp(val context: Context):AlertScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlertItem) {
        val intent = Intent(context,AlertReceiver::class.java)
        intent.putExtra("alertlat",item.lat)
        intent.putExtra("alertlong",item.lon)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC,item.time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000,
            PendingIntent.getBroadcast(context,item.time.hashCode(),intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
    }

    override fun cancel(item: AlertItem) {
       alarmManager.cancel(PendingIntent.getBroadcast(context,item.time.hashCode(),
           Intent(context,AlertReceiver::class.java),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
    }
}