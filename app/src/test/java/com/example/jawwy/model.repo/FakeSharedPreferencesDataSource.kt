package com.example.jawwy.model.repo

import com.example.jawwy.model.sharedprefrence.ISharedPreferenceDatasource

class FakeSharedPreferencesDataSource:ISharedPreferenceDatasource {
    var mylanguage:String = ""
    override fun getNotificationSettings(): String {
        TODO("Not yet implemented")
    }

    override fun getLocationSettings(): String {
        TODO("Not yet implemented")
    }

    override fun getLatitude(): Double {
        TODO("Not yet implemented")
    }

    override fun getLongitude(): Double {
        TODO("Not yet implemented")
    }

    override fun getUnit(): String {
        TODO("Not yet implemented")
    }

    override fun getLanguage(): String {
        if (mylanguage==""){
            return "en"
        }else{
            return mylanguage
        }
    }

    override fun putNotificationSettings(nkey: String) {
        TODO("Not yet implemented")
    }

    override fun putLocationSettings(key: String) {
        TODO("Not yet implemented")
    }

    override fun putLatitude(lat: Double) {
        TODO("Not yet implemented")
    }

    override fun putLongitude(long: Double) {
        TODO("Not yet implemented")
    }

    override fun putUnit(unit: String) {
        TODO("Not yet implemented")
    }

    override fun putLanguage(language: String) {
       mylanguage=language
    }

}
