package com.example.jawwy.currentweather.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
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
import java.util.Locale

private const val TAG = "CurrentWeatherViewModel"
class CurrentWeatherViewModel(private val repository: IWeatherRepository,val context: Context) : ViewModel(){
    private var _weatherobj= MutableStateFlow<WeatherApiState>(WeatherApiState.Loading)
    val weatherobj = _weatherobj
    //var currpojo : JsonPojo

    fun fetchWeather(isfromgps:Boolean = false){
        Log.i("TEST", "fetchWeather: ")
        val lat = repository.getLatitude()
        val long = repository.getLongitude()
        if (isOnline(context)) {
            getWeather(
                lat, long,
                "standard",
                repository.getLanguage(),
                isfromgps
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
    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }


    private fun getWeather(
        lat: Double,
        long: Double,
        units: String = "metric",
        language: String = "en",
        isfromgps:Boolean
    ){
        viewModelScope.launch(Dispatchers.IO) {
            // delete the saved one
            if (isfromgps) {
                repository.deleteFromGps()
            }
            repository.getWeather(lat, long,units,language).collectLatest { data ->
                if (data.isSuccessful){
                    val mypojo = data.body() as JsonPojo
                    val address=getAddress(mypojo.lat!!,mypojo.lon!!)
                    mypojo.city=address.locality ?: ""
                    mypojo.country=address.countryName ?: ""
                    _weatherobj.value = WeatherApiState.Success(data.body()!!)
                    if (isfromgps){
                        mypojo.gps="yes"
                    }else{
                        mypojo.gps="no"
                    }
                    saveWeather(mypojo)
                    Log.i(TAG, "getWeather: "+data.body()?.current?.dt)
                    Log.i(TAG, "getWeather: "+data.body()?.current?.temp)
                }else{
                    Log.i(TAG, "getWeather: Failed")
                }
            }
        }
    }
    //        val lat:Double = repository.getLatitude()
//        val long:Double = repository.getLongitude()

    fun getWeatherByLatLong(lat:Double,long:Double){
        getWeather(lat = lat, long = long, isfromgps = false)
    }


    private suspend fun getAddress(lat:Double, long:Double): Address {
        return repository.getAddress(lat, long)
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

    fun getNotificationSettings():String{
        return repository.getNotificationSettings()
    }

   fun getLocationSettings():String{
       return repository.getLocationSettings()
   }
    fun getUnit():String{
        return repository.getUnit()
    }
    fun putNotificationSettings(nkey:String){
        repository.putNotificationSettings(nkey)
    }
    fun putLocationSettings(key:String){
        repository.putLocationSettings(key)
    }
    fun putlat(lat:Double){
        repository.putLatitude(lat)
    }
    fun putLong(long:Double){
        repository.putLongitude(long)
    }
}