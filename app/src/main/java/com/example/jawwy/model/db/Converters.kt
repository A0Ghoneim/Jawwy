package com.example.jawwy.model.db

import androidx.room.TypeConverter
import com.example.jawwy.model.data.Alerts
import com.example.jawwy.model.data.Current
import com.example.jawwy.model.data.Daily
import com.example.jawwy.model.data.FeelsLike
import com.example.jawwy.model.data.Hourly
import com.example.jawwy.model.data.Temp
import com.example.jawwy.model.data.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val gson = Gson()

    companion object {
         val formatter: DateTimeFormatter =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let {
            return try {
                LocalDateTime.parse(it, formatter)
            } catch (e: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
    @TypeConverter
    fun fromCurrentWeather(currentWeather: Current?): String {
        return currentWeather.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toCurrentWeather(currentWeatherString: String): Current? {
        return currentWeatherString.let {
            gson.fromJson(it, Current::class.java)
        }
    }
    @TypeConverter
    fun fromHourlyList(hourlyList: ArrayList<Hourly>?): String? {
        return gson.toJson(hourlyList)
    }

    @TypeConverter
    fun toHourlyList(hourlyListString: String?): ArrayList<Hourly>? {
        return gson.fromJson(hourlyListString, object : TypeToken<ArrayList<Hourly>>() {}.type)
    }
    @TypeConverter
    fun fromDailyList(dailyList: ArrayList<Daily>?): String? {
        return gson.toJson(dailyList)
    }

    @TypeConverter
    fun toDailyList(dailyListString: String?): ArrayList<Daily>? {
        return gson.fromJson(dailyListString, object : TypeToken<ArrayList<Daily>>() {}.type)
    }
    @TypeConverter
    fun fromAlertsList(alertsList: ArrayList<Alerts>?): String? {
        return gson.toJson(alertsList)
    }

    @TypeConverter
    fun toAlertsList(alertsListString: String?): ArrayList<Alerts>? {
        val type = object : TypeToken<ArrayList<Alerts>>() {}.type
        if (gson.fromJson<ArrayList<Alerts>>(alertsListString, type) !=null) {
            return gson.fromJson(alertsListString, type)
        }
        else{
            return arrayListOf()
        }
    }
    @TypeConverter
    fun fromStringList(stringList: ArrayList<String>?): String? {
        return gson.toJson(stringList)
    }

    @TypeConverter
    fun toStringList(stringListString: String?): ArrayList<String>? {
        return gson.fromJson(stringListString, object : TypeToken<ArrayList<String>>() {}.type)
    }
    @TypeConverter
    fun fromWeatherList(weatherList: ArrayList<Weather>): String {
        return gson.toJson(weatherList)
    }

    @TypeConverter
    fun toWeatherList(weatherListString: String): ArrayList<Weather> {
        val type = object : TypeToken<ArrayList<Weather>>() {}.type
        return gson.fromJson(weatherListString, type)
    }
    @TypeConverter
    fun fromFeelsLike(feelsLike: FeelsLike): String {
        return gson.toJson(feelsLike)
    }

    @TypeConverter
    fun toFeelsLike(feelsLikeString: String): FeelsLike {
        return gson.fromJson(feelsLikeString, FeelsLike::class.java)
    }
    @TypeConverter
    fun fromTemp(temp: Temp): String {
        return gson.toJson(temp)
    }

    @TypeConverter
    fun toTemp(tempString: String): Temp {
        return gson.fromJson(tempString, Temp::class.java)
    }
}