package com.example.jawwy.model.sharedprefrence

import org.intellij.lang.annotations.Language
import java.util.concurrent.TimeUnit

interface ISharedPreferenceDatasource {

    fun getKey():String
    fun getLatitude():Double
    fun getLongitude():Double
    fun getUnit():String
    fun getLanguage():String

    fun putKey(key:String)
    fun putLatitude(lat:Double)
    fun putLongitude(long:Double)
    fun putUnit(unit: String)
    fun putLanguage(language: String)

}
