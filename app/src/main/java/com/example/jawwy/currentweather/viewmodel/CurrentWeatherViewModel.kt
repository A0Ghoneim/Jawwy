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
        val lat = repository.getLatitude()
        val long = repository.getLongitude()
        if (isOnline(context)) {
            getWeather(
                lat, long,
                repository.getUnit(),
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
    fun Double.round(decimals: Int): Double {
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
                    _weatherobj.value = WeatherApiState.Success(data.body()!!)
                    val mypojo = data.body() as JsonPojo
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
    fun getAddress(context: Context,lat:Double,long:Double): Address {
//        val lat:Double = repository.getLatitude()
//        val long:Double = repository.getLongitude()
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        addresses = geocoder.getFromLocation(lat,long,1)
        // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        return addresses!![0]
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

   fun getLocationSettings():String{
       return repository.getLocationSettings()
   }
    fun getUnit():String{
        return repository.getUnit()
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