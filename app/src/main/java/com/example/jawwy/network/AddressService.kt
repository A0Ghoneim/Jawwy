package com.example.jawwy.network

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.Locale

class AddressService(context: Context) {
    val geocoder by lazy {
        Geocoder(context, Locale.getDefault())
    }
    suspend fun getAddress(lat:Double,long:Double):Address{
        val addresses: List<Address>? = geocoder.getFromLocation(lat,long,1)
        return addresses!![0]
    }
}