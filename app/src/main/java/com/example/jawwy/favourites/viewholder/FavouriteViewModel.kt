package com.example.jawwy.favourites.viewholder

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jawwy.currentweather.viewmodel.WeatherApiState
import com.example.jawwy.favourites.FavouriteApiState
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.repo.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class FavouriteViewModel(private val repository: IWeatherRepository) : ViewModel() {
    private var _deleteResult = MutableLiveData<Int>()
    val deleteResult = _deleteResult
    private var _weatherList= MutableStateFlow<FavouriteApiState>(FavouriteApiState.Loading)
    val weatherList = _weatherList


    fun deletefav(w:JsonPojo){
        viewModelScope.launch(Dispatchers.IO) {
            _deleteResult.postValue(repository.delete(w))
        }
    }

    fun getAddress(jsonPojo: JsonPojo,context: Context):Address {
        val lat:Double = jsonPojo.lat ?: 0.0
        val long:Double = jsonPojo.lon ?: 0.0
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
            addresses = geocoder.getFromLocation(lat,long,1)
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
               return addresses!![0]
    }

    fun getAllWeather(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllWeather().collectLatest {
                _weatherList.value=FavouriteApiState.Success(it)
            }
        }
    }
}