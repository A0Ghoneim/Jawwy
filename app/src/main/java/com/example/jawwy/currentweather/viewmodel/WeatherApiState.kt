package com.example.jawwy.currentweather.viewmodel

import com.example.jawwy.model.data.JsonPojo

sealed
class WeatherApiState {
    class Success(val data:JsonPojo) : WeatherApiState()
    class Failure(val msg:Throwable) : WeatherApiState()
    object Loading : WeatherApiState()
}
