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

    override fun schedule(time: LocalDateTime) {
        val intent = Intent(context,AlertReceiver::class.java)
        intent.putExtra("MESSAGE","messenger")
        Log.i("TAG", "schedule: "+time.toString()+"  "+time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000)
        Log.i("TAG", "schedule: "+time.toString()+"  "+LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()*1000)
        Log.i("TAG", "schedule: "+time.toString()+"  "+(time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000).minus(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()*1000))
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC,time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000,
            PendingIntent.getBroadcast(context,time.hashCode(),intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
    }

    override fun cancel(time: LocalDateTime) {
       alarmManager.cancel(PendingIntent.getBroadcast(context,time.hashCode(),
           Intent(context,AlertReceiver::class.java),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
    }
}