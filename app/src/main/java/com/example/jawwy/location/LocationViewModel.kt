package com.example.jawwy.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jawwy.model.repo.IWeatherRepository
import com.google.android.gms.location.LocationResult

class LocationViewModel(private val repository: IWeatherRepository) : ViewModel() {
    private var _locationResult = MutableLiveData<LocationResult>()
    val locationResult = _locationResult
    fun getLocation(){

    }
}