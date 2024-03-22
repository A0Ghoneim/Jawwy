package com.example.jawwy.model.sharedprefrence

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferenceDatasource private constructor(val context: Context) :
    ISharedPreferenceDatasource {

    val sharedPreference : SharedPreferences by lazy {
        context.getSharedPreferences("mypref", Context.MODE_PRIVATE)
    }
    val editor : SharedPreferences.Editor by lazy {
        sharedPreference.edit()
    }

    companion object {
        @Volatile
        private var Instance: ISharedPreferenceDatasource? = null

        @Synchronized
        fun getInstance(context: Context): ISharedPreferenceDatasource {
            return Instance ?: synchronized(this) {
                val instance = SharedPreferenceDatasource(context)
                Instance = instance
                instance
            }
        }
    }

    override fun getKey(): String {
        return sharedPreference.getString("key","no")!!
    }

    override fun getLatitude(): Double {
        return getDouble(sharedPreference,"lat",0.0)
    }

    override fun getLongitude(): Double {
       return getDouble(sharedPreference,"long",0.0)
    }

    override fun getUnit(): String {
        return sharedPreference.getString("temperature","standard")!!
    }

    override fun getLanguage(): String {
        return sharedPreference.getString("language","en")!!
    }

    override fun putKey(key: String) {
        editor.putString("key",key).apply()
    }

    override fun putLatitude(lat: Double) {
        putDouble(editor,"lat",lat)
    }

    override fun putLongitude(long: Double) {
        putDouble(editor,"long",long)
    }

    override fun putUnit(unit: String) {
        editor.putString("temperature",unit).apply()
    }

    override fun putLanguage(language: String) {
        editor.putString("language",language).apply()
    }

    private fun putDouble(edit: SharedPreferences.Editor, key: String?, value: Double){
        return edit.putLong(key, java.lang.Double.doubleToRawLongBits(value)).apply()
    }

    private fun getDouble(prefs: SharedPreferences, key: String?, defaultValue: Double): Double {
        return java.lang.Double.longBitsToDouble(
            prefs.getLong(
                key,
                java.lang.Double.doubleToLongBits(defaultValue)
            )
        )
    }
}