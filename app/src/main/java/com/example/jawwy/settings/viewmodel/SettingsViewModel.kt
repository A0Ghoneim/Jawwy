package com.example.jawwy.settings.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import com.example.jawwy.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE
import com.example.jawwy.model.repo.IWeatherRepository

class SettingsViewModel(private val repository: IWeatherRepository) : ViewModel() {
    fun putUnit(unit:String){
        repository.putUnit(unit)
    }
    fun getUnit():String{
        return repository.getUnit()
    }

    fun getLanguage():String{
        return repository.getLanguage()
    }
    fun putLanguage(language: String){
        repository.putLanguage(language)
    }
    fun getLocationSettings():String{
        return repository.getLocationSettings()
    }

    fun putLocationSettings(key:String){
        repository.putLocationSettings(key)
    }
    fun getNotificationSettings():String{
        return repository.getNotificationSettings()
    }

    fun putNotificationSettings(nkey:String){
        repository.putNotificationSettings(nkey)
    }

//    private  fun requestPermission(context: Context): Boolean {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(context)) {
//                val mypkg = context.packageName
//                val intent = Intent(
//                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:$mypkg")
//                )
//                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
//                return true
//            }
//        }
//        return false
//    }
}