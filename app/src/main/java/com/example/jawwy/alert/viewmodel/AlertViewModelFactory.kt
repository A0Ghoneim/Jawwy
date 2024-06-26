package com.example.jawwy.alert.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jawwy.model.repo.IWeatherRepository

class AlertViewModelFactory(private val repository: IWeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.i("TEST", "factory create: ")
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(repository) as T
        }
        else{
            throw IllegalArgumentException("illegal")
        }
    }
}