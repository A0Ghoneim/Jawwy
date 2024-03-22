package com.example.jawwy.currentweather.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jawwy.model.repo.IWeatherRepository

class CurrentWeatherVieModelFactory(private val repository: IWeatherRepository,private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java)){
            CurrentWeatherViewModel(repository,context) as T
        }
        else{
            throw IllegalArgumentException("illegal")
        }
    }
}