package com.example.jawwy.alert.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jawwy.alert.AlertItem
import com.example.jawwy.favourites.FavouriteApiState
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.repo.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class AlertViewModel(private val repository: IWeatherRepository):ViewModel() {
    private var _deleteResult = MutableLiveData<Int>()
    val deleteResult = _deleteResult
    private var _insertResult = MutableLiveData<Long>()
    val insertResult = _insertResult
    private var _alertList= MutableStateFlow<AlertApiState>(AlertApiState.Loading)
    val alertList = _alertList


    fun deleteAlert(a: AlertItem){
        viewModelScope.launch(Dispatchers.IO) {
            _deleteResult.postValue(repository.deleteAlert(a))
        }
    }

    fun insertAlert(a: AlertItem){
        viewModelScope.launch(Dispatchers.IO) {
            _insertResult.postValue(repository.insertAlert(a))
        }
    }

    fun getAddress(context: Context): Address {
        val lat:Double = repository.getLatitude()
        val long:Double = repository.getLongitude()
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        addresses = geocoder.getFromLocation(lat,long,1)
        // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        return addresses!![0]
    }

    fun getAllAlerts(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllAlerts().collectLatest {
                _alertList.value= AlertApiState.Success(it)
            }
        }
    }
}