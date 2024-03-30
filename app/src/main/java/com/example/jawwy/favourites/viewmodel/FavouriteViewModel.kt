package com.example.jawwy.favourites.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jawwy.favourites.FavouriteApiState
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.repo.IWeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class FavouriteViewModel(private val repository: IWeatherRepository,val dispatcher: CoroutineDispatcher) : ViewModel() {
    private var _deleteResult = MutableLiveData<Int>()
    val deleteResult = _deleteResult
    private var _weatherList= MutableStateFlow<FavouriteApiState>(FavouriteApiState.Loading)
    val weatherList = _weatherList


    fun deletefav(w:JsonPojo){
        viewModelScope.launch(dispatcher) {
            _deleteResult.postValue(repository.delete(w))
        }
    }


    fun getAllWeather(){
        viewModelScope.launch(dispatcher) {
            repository.getAllWeather().collectLatest {
                _weatherList.value=FavouriteApiState.Success(it)
            }
        }
    }
}