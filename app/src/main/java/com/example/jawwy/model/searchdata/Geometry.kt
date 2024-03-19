package com.example.jawwy.model.searchdata

import com.google.gson.annotations.SerializedName


data class Geometry (

    @SerializedName("coordinates" ) var coordinates : ArrayList<Double> = arrayListOf(),
    @SerializedName("type"        ) var type        : String?           = null

)
