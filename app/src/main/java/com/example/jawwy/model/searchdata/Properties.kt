package com.example.jawwy.model.searchdata

import com.google.gson.annotations.SerializedName


data class Properties (

    @SerializedName("osm_type"    ) var osmType     : String?           = null,
    @SerializedName("osm_id"      ) var osmId       : Long?              = null,
    @SerializedName("extent"      ) var extent      : ArrayList<Double> = arrayListOf(),
    @SerializedName("country"     ) var country     : String?           = null,
    @SerializedName("osm_key"     ) var osmKey      : String?           = null,
    @SerializedName("countrycode" ) var countrycode : String?           = null,
    @SerializedName("city")       var  city :String?=null,
    @SerializedName("osm_value"   ) var osmValue    : String?           = null,
    @SerializedName("name"        ) var name        : String?           = null,
    @SerializedName("type"        ) var type        : String?           = null

)
