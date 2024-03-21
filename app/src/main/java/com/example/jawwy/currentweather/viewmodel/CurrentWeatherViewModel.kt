package com.example.jawwy.currentweather.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jawwy.model.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "CurrentWeatherViewModel"
class CurrentWeatherViewModel(private val repository: IWeatherRepository) : ViewModel(){

    fun getWeather(
        lat: Double,
        long: Double,
        units: String = "metric",
        language: String = "en"
    ){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeather(lat, long,units,language).collectLatest { data ->
                if (data.isSuccessful){
                    Log.i(TAG, "getWeather: "+data.body()?.current?.dt)
                    Log.i(TAG, "getWeather: "+data.body()?.current?.temp)
                }else{
                    Log.i(TAG, "getWeather: Failed")
                }
            }
        }
    }
}