package com.example.jawwy

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ColorFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jawwy.alert.view.AlertsActivity
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherVieModelFactory
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherViewModel
import com.example.jawwy.currentweather.viewmodel.WeatherApiState
import com.example.jawwy.databinding.ActivityMainBinding
import com.example.jawwy.favourites.FavouritesActivity
import com.example.jawwy.mainadapters.DailyAdapter
import com.example.jawwy.mainadapters.HourAdapter
import com.example.jawwy.model.data.Current
import com.example.jawwy.model.data.Daily
import com.example.jawwy.model.data.Hourly
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.db.WeatherLocalDataSource
import com.example.jawwy.model.repo.WeatherRepository
import com.example.jawwy.model.sharedprefrence.SharedPreferenceDatasource
import com.example.jawwy.network.WeatherRemoteDataSource
import com.example.jawwy.settings.view.SettingsActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.animation.AnimationUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.round


private const val My_LOCATION_PERMISSION_ID = 5005
const val CELSIUS="°C"
const val FAHRENHEIT="°F"
const val KELVIN="°K"
const val METER="meter/s"
const val MILE="mile/h"
const val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 777

class MainActivity : AppCompatActivity() {
    lateinit var act: MainActivity
    lateinit var ctx: Context
    lateinit var viewModel: CurrentWeatherViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var factory: CurrentWeatherVieModelFactory
    private var gpsFlag = true
    private lateinit var locationCallback: LocationCallback
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    lateinit var hourlyAdapter:HourAdapter
    lateinit var dailyAdapter: DailyAdapter

    // lateinit var sharedPref:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /////////////////UI/////////////////////////
            drawHourly(arrayListOf(), KELVIN, METER)
            drawDaily(arrayListOf(), KELVIN)

        val anim =android.view.animation.AnimationUtils.loadAnimation(this,R.anim.popup_anim)


        ////////////////UI//////////////////////////


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.areNotificationsEnabled()) {

        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }
            //work manager
       // requestPermission()



        act = this
        ctx = this
        factory = CurrentWeatherVieModelFactory(
            WeatherRepository(
                WeatherRemoteDataSource,
                WeatherLocalDataSource.getInstance(ctx),
                SharedPreferenceDatasource.getInstance(ctx)
            ), ctx
        )
        viewModel = ViewModelProvider(this, factory).get(CurrentWeatherViewModel::class.java)
        //   sharedPref = act.getSharedPreferences("mypref",Context.MODE_PRIVATE)


        Log.i("TAG", "onCreate: isfirst $gpsFlag")

        lifecycleScope.launch {
            viewModel.weatherobj.collectLatest { result ->
                when (result) {
                    is WeatherApiState.Success -> {
                        drawCurrent(result.data,this@MainActivity)
                       binding.progressBar.visibility = View.GONE
                        binding.dayBox.visibility = View.VISIBLE
                        binding.statisticsBox.visibility = View.VISIBLE
                        binding.pressureanimationView.startAnimation(anim)
                        binding.humidityanimationView.startAnimation(anim)
                        binding.windanimationView.startAnimation(anim)


//                       var myadapter= ProductAdapter(act, result.data as ArrayList<Product>,act, R.layout.list_row, ADD)
//                       binding.recyclerView.adapter=myadapter
                        Log.i(
                            "create",
                            "onCreate: Main " + result.data.id + " " + result.data.current?.dt + " " + result.data.lat + " " + result.data.lon + " " + result.data.current?.temp
                        )
                    }

                    is WeatherApiState.Failure -> {
                         binding.progressBar.visibility = View.GONE
                    }

                    is WeatherApiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.animationView.visibility = View.GONE
                    }
                }
            }
        }

        binding.favImage.setOnClickListener {
            startActivity(Intent(act, FavouritesActivity::class.java))
        }
        binding.settingsImage.setOnClickListener {
            startActivity(Intent(act, SettingsActivity::class.java))
        }
        binding.alertFloatingActionButton.setOnClickListener {
            startActivity(Intent(act, AlertsActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        // val sharedPref = act?.getSharedPreferences("getString(R.string.preference_file_key)", Context.MODE_PRIVATE)
        val key = viewModel.getLocationSettings()

        if (key == "manual") {
            gpsFlag = false
            viewModel.fetchWeather()
        } else {
            //viewModel.putkey("GPS")
            gpsFlag = true
        }
        if (gpsFlag) {
            if (checkPermissions()) {
                if (isLocationEnabled(this)) {
                    getLocation()
                } else {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    My_LOCATION_PERMISSION_ID
                )
            }
        }
    }

    fun checkPermissions(): Boolean {
        var result = false
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
            ||
            (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            result = true
        }
        return result
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        if (requestCode == My_LOCATION_PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isLocationEnabled(this)) {
                    getLocation()
                } else {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationRequest = LocationRequest.Builder(0).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        }.build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                var lat = 0.0
                var long = 0.0
                if (locationResult.lastLocation != null) {
                    lat = locationResult.lastLocation!!.latitude
                    long = locationResult.lastLocation!!.longitude
                    Log.i("onResult", "onLocationResult: " + lat + " " + long)
                    Toast.makeText(baseContext, "lat $lat", Toast.LENGTH_SHORT).show()

                    viewModel.putlat(lat)
                    viewModel.putLong(long)
                    ////////
                    viewModel.fetchWeather(true)
                    ///////
                    mFusedLocationClient.removeLocationUpdates(locationCallback)
                }


            }
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()
        )
    }


    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            locationMode = try {
                Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            locationMode != Settings.Secure.LOCATION_MODE_OFF
        } else {
            locationProviders = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            !TextUtils.isEmpty(locationProviders)
        }
    }

    fun drawCurrent(jsonPojo: JsonPojo,context: Context) {
            val address = viewModel.getAddress(context,jsonPojo.lat!!,jsonPojo.lon!!)
        val city:String = address.locality ?:""
        val country:String = address.countryName ?:""
        val symbol = when(viewModel.getUnit()){
            "metric" -> CELSIUS
            "imperial" -> FAHRENHEIT
            "standard" -> KELVIN
            else -> KELVIN
        }
        val speedUnit = when(viewModel.getUnit()){
            "metric" -> METER
            "imperial" -> MILE
            "standard" -> METER
            else -> METER
        }
        val current:Current = jsonPojo.current ?:Current()
        when(jsonPojo.daily[0].weather[0].main){
            "Rain" -> {binding.animationView.setAnimation(R.raw.animationrainy)
                binding.animationView.setPadding(0,-1000,0,0)
                        binding.animationView.visibility = View.VISIBLE}
            "Snow" -> {binding.animationView.setAnimation(R.raw.animationsnoww)
                binding.animationView.setPadding(0,-2100,0,0)
                binding.animationView.visibility = View.VISIBLE}
            "Clouds" -> {binding.animationView.setAnimation(R.raw.animationgreyclouds)
                binding.animationView.visibility = View.VISIBLE}
        }
        val pressure = current.pressure ?: 0
        val humidity = current.humidity ?: 0
        var windSpeed = current.windSpeed ?: 0.0
        if (speedUnit== MILE){
            windSpeed = UnitConverter.meterPerSecondToMilesPerHour(windSpeed)
        }
        var d = current.temp ?: 0.0
        if (symbol== CELSIUS){
            d= UnitConverter.kelvinToCelsius(d)
        }else if (symbol == FAHRENHEIT){
            d= UnitConverter.kelvinToFahrenheit(d)
        }
        val degree:Int = d.toInt()
        val state = current.weather[0].description ?:""
        val icon = current.weather[0].icon
        val link = "https://openweathermap.org/img/wn/$icon@2x.png"
        binding.locationText.text="$city/$country"
        binding.degreeText.text="$degree$symbol"
        binding.stateText.text="$state"
        binding.dateTextView.text=getDateDetailsFromUnixTimestamp(current.dt!!)
        animateTextView(0,pressure,binding.pressureDigit)
        animateTextView(0,humidity,binding.humidityDigit)
        animateTextView(0.0,windSpeed,binding.windDigit)
        binding.windUnit.text=speedUnit
        Log.i("Icon", "drawCurrent: $link")
        Glide.with(context).load(link)
            .apply(RequestOptions().override(100, 100)).into(binding.currIcon)
        val first24hourList = arrayListOf<Hourly>()
        val fullHourlyList = jsonPojo.hourly
        for (i in 0..23){
            first24hourList.add(fullHourlyList[i])
        }
        updateHourly(first24hourList,symbol,speedUnit)
        updateDaily(jsonPojo.daily,symbol)
    }
    fun drawHourly(hourlyList: MutableList<Hourly>, symbol: String, speedUnit: String){
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation= LinearLayoutManager.HORIZONTAL
        binding.hourlyRecycler.layoutManager=linearLayoutManager
        hourlyAdapter= HourAdapter(hourlyList,symbol,speedUnit)
        binding.hourlyRecycler.adapter=hourlyAdapter
    }
    fun updateHourly(hourlyList: MutableList<Hourly>, symbol: String, speedUnit: String){
        hourlyAdapter.updateList(hourlyList,symbol,speedUnit)
    }

    fun drawDaily(dailyList: MutableList<Daily>, symbol: String){
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
        binding.dayRecycler.layoutManager=linearLayoutManager
        dailyAdapter=DailyAdapter(dailyList,symbol)
        binding.dayRecycler.adapter=dailyAdapter
    }
    fun updateDaily(dailyList: MutableList<Daily>, symbol: String){
        dailyAdapter.updateList(dailyList,symbol)
    }

    fun animateTextView(initialValue: Int, finalValue: Int, textview: TextView) {
        val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue)
        valueAnimator.setDuration(3000)
        valueAnimator.addUpdateListener { valueAnimator ->
            textview.text = valueAnimator.animatedValue.toString()
        }
        valueAnimator.start()
    }
    fun animateTextView(initialValue: Double, finalValue: Double, textview: TextView) {
        val valueAnimator = ValueAnimator.ofFloat(initialValue.toFloat(),finalValue.toFloat())
        valueAnimator.setDuration(2000)
        valueAnimator.addUpdateListener { valueAnimator ->
            val roundedValue = round(valueAnimator.animatedValue.toString().toDouble() * 1000) / 1000 // Round to 3 decimal places
            textview.text = roundedValue.toString()
        }
        valueAnimator.start()
    }
    fun getDateDetailsFromUnixTimestamp(unixTimestamp: Int): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(unixTimestamp.toLong()),
            ZoneId.systemDefault()
        )

        val weekday = localDateTime.dayOfWeek.toString() // Day of week (e.g., "MONDAY")
        val dayOfMonth = localDateTime.dayOfMonth // Day of month (e.g., 25)
        val month = localDateTime.month.toString() // Month (e.g., "MARCH")

        return "$weekday, $dayOfMonth $month"
    }


}