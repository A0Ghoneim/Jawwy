package com.example.jawwy.model.searchdata

import com.google.gson.annotations.SerializedName


data class SearchPojo (

    @SerializedName("features" ) var features : ArrayList<Features> = arrayListOf(),
    @SerializedName("type"     ) var type     : String?             = null

)