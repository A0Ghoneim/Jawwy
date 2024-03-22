package com.example.jawwy.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Temp (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") var id: Int = 0,
    @SerializedName("day"   ) var day   : Double? = null,
    @SerializedName("min"   ) var min   : Double? = null,
    @SerializedName("max"   ) var max   : Double? = null,
    @SerializedName("night" ) var night : Double? = null,
    @SerializedName("eve"   ) var eve   : Double? = null,
    @SerializedName("morn"  ) var morn  : Double? = null

)
