package com.example.jawwy.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class FeelsLike (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") var id: Int = 0,
    @SerializedName("day"   ) var day   : Double? = null,
    @SerializedName("night" ) var night : Double? = null,
    @SerializedName("eve"   ) var eve   : Double? = null,
    @SerializedName("morn"  ) var morn  : Double? = null

)
