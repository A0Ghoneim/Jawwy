package com.example.jawwy.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.jawwy.model.db.Converters
import com.google.gson.annotations.SerializedName

@Entity
@TypeConverters(Converters::class)

data class JsonPojo (
    @PrimaryKey
    @SerializedName("id") var id: String,
    @SerializedName("lat"             ) var lat            : Double?           = null,
    @SerializedName("lon"             ) var lon            : Double?           = null,
    @SerializedName("timezone"        ) var timezone       : String?           = null,
    @SerializedName("timezone_offset" ) var timezoneOffset : Int?              = null,
    @SerializedName("current"         ) var current        : Current?          = Current(),
    @SerializedName("hourly"          ) var hourly         : ArrayList<Hourly> = arrayListOf(),
    @SerializedName("daily"           ) var daily          : ArrayList<Daily>  = arrayListOf(),
    @SerializedName("alerts"          ) var alerts         : ArrayList<Alerts> = arrayListOf(),
    @SerializedName("gps"             ) var gps            : String,
    @SerializedName("city"             ) var city            : String="",
    @SerializedName("country"             ) var country            : String=""


)