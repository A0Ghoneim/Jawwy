package com.example.jawwy.search.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jawwy.model.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: IWeatherRepository) : ViewModel()  {
    private var _featureList= MutableStateFlow<ApiState>(ApiState.Loading)
    val featureList = _featureList
    fun search(place : String,limit : Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.search(place,limit).collectLatest { data ->
                if (data.isSuccessful){
                    _featureList.value = ApiState.Success(data.body()!!.features)
                    //Log.i(TAG, "getWeather: "+data.body()?.current?.dt)
                }else{
                    //Log.i(TAG, "getWeather: Failed")
                }
            }
        }
    }
}