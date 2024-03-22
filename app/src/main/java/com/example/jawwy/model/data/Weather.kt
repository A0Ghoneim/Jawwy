package com.example.jawwy.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Weather (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id"          ) var id          : Int?    = null,
    @SerializedName("main"        ) var main        : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("icon"        ) var icon        : String? = null

)
