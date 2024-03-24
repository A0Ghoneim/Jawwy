package com.example.jawwy.alert

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.jawwy.R
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherVieModelFactory
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherViewModel
import com.example.jawwy.currentweather.viewmodel.WeatherApiState
import com.example.jawwy.model.db.WeatherLocalDataSource
import com.example.jawwy.model.repo.WeatherRepository
import com.example.jawwy.model.sharedprefrence.SharedPreferenceDatasource
import com.example.jawwy.network.WeatherRemoteDataSource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.math.log

class AlertReceiver:BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val message:String = intent?.getStringExtra("MESSAGE") ?: return

        val viewModel=CurrentWeatherViewModel(WeatherRepository(
            WeatherRemoteDataSource,
            WeatherLocalDataSource.getInstance(context!!),
            SharedPreferenceDatasource.getInstance(context)
        ), context)

        viewModel.fetchWeather()

        Log.i("TAG", "onReceive: Nooooooooooo")

        @OptIn(DelicateCoroutinesApi::class) // Must run globally; there's no teardown callback.
        GlobalScope.launch(Dispatchers.IO) {
            try {
               viewModel.weatherobj.collectLatest { result ->
                   when (result) {
                       is WeatherApiState.Success -> {
                           if (result.data.current==null){
                               Log.i("Alerts", "onReceive: No Alerts")
                               notificationManager.notify(10,createNotidication(context,"No Alerts","Enjoy a peaceful day").build())
                           }
                           else{
                               Log.i("Alerts", "onReceive: "+ result.data.current?.temp)
                               notificationManager.notify(10,createNotidication(context,"${result.data.timezone} Alerts","descrivtionnnnnn").build())


                           }
//                           Log.i(
//                               "create",
//                               "onCreate: Main " + result.data.id + " " + result.data.current?.dt + " " + result.data.lat + " " + result.data.lon + " " + result.data.current?.temp
//                           )
                       }

                       is WeatherApiState.Failure -> {
                           // binding.progressBar2.visibility = View.GONE
                       }

                       is WeatherApiState.Loading -> {
                           //binding.progressBar2.visibility = View.VISIBLE }
                       }
                   }
               }
            } finally {
            }
        }

   }
    fun createNotidication(context: Context,title:String,describtion:String): NotificationCompat.Builder{
        //  val intent = Intent(context,)
        createNotificationChannel(context)
// some code goes here
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, "15")
                .setSmallIcon(R.drawable.baseline_add_alert_24)
         //       .setOngoing(true)
                .setContentTitle(title)
                .setContentText(describtion)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // .setContentIntent(pendingIntent)
                .setAutoCancel(false)
        return builder
    }
    fun createNotificationChannel(context: Context) {
// Create the NotificationChannel, but only on API 26+ because
// the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_name"
            val description: String ="channel_description"
            val importance:Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("15", name, importance)
            channel.description = description
// Register the channel with the system; you can't change the importance
// or other notification behaviors after this
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }}

}