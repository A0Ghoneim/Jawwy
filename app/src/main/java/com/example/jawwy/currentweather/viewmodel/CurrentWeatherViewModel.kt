package com.example.jawwy.currentweather.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.repo.IWeatherRepository
import com.example.jawwy.search.ViewModel.FeatureApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "CurrentWeatherViewModel"
class CurrentWeatherViewModel(private val repository: IWeatherRepository,val context: Context) : ViewModel(){
    private var _weatherobj= MutableStateFlow<WeatherApiState>(WeatherApiState.Loading)
    val weatherobj = _weatherobj
    //var currpojo : JsonPojo

    fun fetchWeather(){
        val lat = repository.getLatitude()
        val long = repository.getLongitude()
        if (isOnline(context)) {
            getWeather(
                lat, long,
                repository.getUnit(),
                repository.getLanguage()
            )
           // currpojo?.let { saveWeather(it) }
        }
        else{
            val roundedlat = lat.round(4)
            val roundedlong = long.round(4)
            val string = "$roundedlat"+"$roundedlong"
            Log.i(TAG, "fetchWeather: string "+string)
            getSavedWeather(string)
        }
    }
    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }

    private fun getWeather(
        lat: Double,
        long: Double,
        units: String = "metric",
        language: String = "en"
    ){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeather(lat, long,units,language).collectLatest { data ->
                if (data.isSuccessful){
                    _weatherobj.value = WeatherApiState.Success(data.body()!!)
                    saveWeather(data.body()!!)
                    Log.i(TAG, "getWeather: "+data.body()?.current?.dt)
                    Log.i(TAG, "getWeather: "+data.body()?.current?.temp)
                }else{
                    Log.i(TAG, "getWeather: Failed")
                }
            }
        }
    }

    private fun getSavedWeather(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeatherById(id).collectLatest { data ->
                if (data!=null) {
                    Log.i(TAG, "getSavedWeather: "+data .id)
                    _weatherobj.value = WeatherApiState.Success(data)
                }
            }
        }
    }


    private fun saveWeather(jsonPojo: JsonPojo){
        viewModelScope.launch(Dispatchers.IO) {
            jsonPojo.id="${jsonPojo.lat}${jsonPojo.lon}"
            Log.i(TAG, "saveWeather: "+jsonPojo.id)
            repository.insert(jsonPojo)
        }
    }
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

   fun getkey():String{
       return repository.getKey()
   }
    fun putkey(key:String){
        repository.putKey(key)
    }
    fun putlat(lat:Double){
        repository.putLatitude(lat)
    }
    fun putLong(long:Double){
        repository.putLongitude(long)
    }
}