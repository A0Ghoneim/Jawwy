package com.example.jawwy.settings.viewmodel

import androidx.lifecycle.ViewModel
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
        return repository.putLanguage(language)
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

}