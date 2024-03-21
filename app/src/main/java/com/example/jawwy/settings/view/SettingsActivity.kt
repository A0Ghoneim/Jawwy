package com.example.jawwy.settings.view

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.example.jawwy.R
import com.example.jawwy.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding:ActivitySettingsBinding
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val act = this
        sharedPref = act.getSharedPreferences("mypref", Context.MODE_PRIVATE)


        binding.locationGroup.setOnCheckedChangeListener { group, checkedId ->
            val key = "location"
            val value = findViewById<RadioButton>(checkedId).text.toString()
            sharedPref.edit().putString(key, value).apply()
        }
        binding.languageGroup.setOnCheckedChangeListener { group, checkedId ->
            val key = "language"
            val value = findViewById<RadioButton>(checkedId).text.toString()
            val res:String = when(value){
                getString(R.string.arabic) -> "ar"
                getString(R.string.english) -> "en"
                else -> {"en"}
            }
            sharedPref.edit().putString(key, res).apply()
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
            sharedPref.edit().putString(key, res).apply()
        }
        binding.windGroup.setOnCheckedChangeListener { group, checkedId ->
            val key = "wind"
            val value = findViewById<RadioButton>(checkedId).text.toString()
            sharedPref.edit().putString(key, value).apply()
        }
    }
}