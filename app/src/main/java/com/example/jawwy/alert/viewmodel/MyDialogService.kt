package com.example.jawwy.alert.viewmodel

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.TypedValue
import android.view.Window
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.example.jawwy.R

lateinit var mediaPlayer:MediaPlayer
class MyDialogService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var title:String?=intent?.getStringExtra("title")
        var describtion:String?= intent?.getStringExtra("description")
        if (title!=null&&describtion!=null) {
            startForeground(10, createNotidication(this,title,describtion).build())
            showwindowmanager(title,describtion)
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.we_interrupt);
            mediaPlayer.start();

        }
        return super.onStartCommand(intent, flags, startId)
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

//    fun showWindowManager(context: Context) {
//        val p = WindowManager.LayoutParams(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//            PixelFormat.TRANSLUCENT
//        )
//        val windowManager = context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
//        val layoutInflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val popupView: View = layoutInflater.inflate(R.layout.window_manager_layout, null)
//        windowManager.addView(popupView, p)
//
//        // dismiss windowManager after 3s
//        Handler().postDelayed(Runnable { windowManager.removeView(popupView) }, 3000)
//    }
    fun showwindowmanager(title:String,message:String){
    val dialogBuilder: AlertDialog.Builder =
        AlertDialog.Builder(this, R.style.AppTheme_MaterialDialogTheme)

    dialogBuilder.setTitle(title)
    dialogBuilder.setMessage(message)
//    dialogBuilder.setNegativeButton(R.string.btn_back,
//        DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }
//    )

    val dialog: AlertDialog = dialogBuilder.create()
    val dialogWindow: Window? = dialog.getWindow()
    val dialogWindowAttributes = dialogWindow?.attributes

// Set fixed width (280dp) and WRAP_CONTENT height

// Set fixed width (280dp) and WRAP_CONTENT height
    val lp = WindowManager.LayoutParams()
    lp.copyFrom(dialogWindowAttributes)
    lp.width =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280f, resources.displayMetrics)
            .toInt()
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
    dialogWindow?.attributes = lp

// Set to TYPE_SYSTEM_ALERT so that the Service can display it
    dialog.setOnDismissListener {
        mediaPlayer.stop();
        mediaPlayer.release();
        stopSelf()
    }

// Set to TYPE_SYSTEM_ALERT so that the Service can display it
    dialogWindow?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
    //dialogWindowAttributes.windowAnimations = com.example.jawwy.R.style.DialogAnimation
    dialog.show()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}