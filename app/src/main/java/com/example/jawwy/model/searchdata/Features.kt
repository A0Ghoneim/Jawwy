package com.example.jawwy.model.searchdata

import com.google.gson.annotations.SerializedName


data class Features (

    @SerializedName("geometry"   ) var geometry   : Geometry?   = Geometry(),
    @SerializedName("type"       ) var type       : String?     = null,
    @SerializedName("properties" ) var properties : Properties? = Properties()

)
