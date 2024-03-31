package com.example.jawwy.settings.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import com.example.jawwy.currentweather.view.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE
import com.example.jawwy.R
import com.example.jawwy.databinding.ActivitySettingsBinding
import com.example.jawwy.model.db.WeatherLocalDataSource
import com.example.jawwy.model.repo.WeatherRepository
import com.example.jawwy.model.sharedprefrence.SharedPreferenceDatasource
import com.example.jawwy.network.WeatherRemoteDataSource
import com.example.jawwy.settings.viewmodel.SettingsViewModel
import com.example.jawwy.settings.viewmodel.SettingsViewModelFactory
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    lateinit var binding:ActivitySettingsBinding
    lateinit var viewModel:SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val act = this
        val factory = SettingsViewModelFactory(
            WeatherRepository(
                WeatherRemoteDataSource.getInstance(this),
                WeatherLocalDataSource.getInstance(this),
                SharedPreferenceDatasource.getInstance(applicationContext)
            )
        )
        viewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)

        binding.materialSwitch.setOnCheckedChangeListener { _, isChecked ->
            val key = "location"
            if (isChecked) {
                viewModel.putLocationSettings("GPS")
            } else {
                viewModel.putLocationSettings("manual")
            }
        }


        binding.languageGroup.setOnCheckedChangeListener { group, checkedId ->
            val key = "language"
            val value = findViewById<RadioButton>(checkedId).text.toString()
            val res:String = when(value){
                getString(R.string.arabic) -> "ar"
                getString(R.string.english) -> "en"
                else -> {"ar"}
            }

            if (viewModel.getLanguage()!=res){
                viewModel.putLanguage(res)
                    finish()
            }

            // recreate()

        }
        binding.temperatureGroup.setOnCheckedChangeListener { group, checkedId ->
            val key = "temperature"
            val value = findViewById<RadioButton>(checkedId).text.toString()
            val res:String = when(value){
                getString(R.string.celsius) -> "metric"
                getString(R.string.fahrenheit) -> "imperial"
                getString(R.string.kelvin) -> "standard"
                else -> {"standard"}
            }
            when(res){
                "metric" -> if(!binding.metricRbtn.isChecked)binding.metricRbtn.isChecked=true
                "imperial" -> if(!binding.mileRbtn.isChecked)binding.mileRbtn.isChecked=true
                "standard" -> if(!binding.metricRbtn.isChecked)binding.metricRbtn.isChecked=true
            }
            viewModel.putUnit(res)
        }
        binding.windGroup.setOnCheckedChangeListener { group, checkedId ->
            val key = "temperature"
            val value = findViewById<RadioButton>(checkedId).text.toString()
            val res:String = when(value){
                getString(R.string.mile_hour) -> "mile"
                getString(R.string.meter_second) -> "meter"
                else -> {"meter"}
            }
            when(res){
                "mile" -> if(!binding.fahrenheitRbtn.isChecked)binding.fahrenheitRbtn.isChecked=true
                "meter" -> if (binding.fahrenheitRbtn.isChecked){
                    binding.kelvinRbtn.isChecked=true
                }
            }
        }
        binding.alertGroup.setOnCheckedChangeListener { group, checkedId ->
            val key = "notification"
            val value = findViewById<RadioButton>(checkedId).text.toString()
            val res:String = when(value){
                getString(R.string.notification) -> "notwindow"
                getString(R.string.window) -> "window"
                else -> {"notwindow"}
            }
            if (res == "window"){
                requestPermission()
            }
            else {
                viewModel.putNotificationSettings(res)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        when(viewModel.getLocationSettings()){
            "GPS" -> binding.materialSwitch.isChecked=true
        }
        when(viewModel.getLanguage()){
            "en" -> binding.englishRbtn.isChecked=true
            "ar" -> binding.arabicRbtn.isChecked=true
        }

        when(viewModel.getUnit()){
            "metric" -> {
                binding.metricRbtn.isChecked=true
                binding.celsiusRbtn.isChecked=true
            }
            "imperial" -> {
                binding.mileRbtn.isChecked=true
                binding.fahrenheitRbtn.isChecked=true
            }
            "standard" -> {
                binding.metricRbtn.isChecked=true
                binding.kelvinRbtn.isChecked=true
            }
        }
        when(viewModel.getNotificationSettings()){
            "notwindow" -> binding.notificationRbtn.isChecked=true
            "window" -> binding.windoRbtn.isChecked=true
        }
    }
    fun requestPermission(): Boolean {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            return true
        }
        return false
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                viewModel.putNotificationSettings("window")
            }
        }
    }
    private fun getCurrentLocale(context: Context): Locale {
        val configuration = context.resources.configuration
        return configuration.locales.get(0)
    }

}