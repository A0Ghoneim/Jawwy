package com.example.jawwy.model.data

import com.google.gson.annotations.SerializedName


data class ErrorJsonPojo (

    @SerializedName("cod"        ) var cod        : Int?              = null,
    @SerializedName("message"    ) var message    : String?           = null,
    @SerializedName("parameters" ) var parameters : ArrayList<String> = arrayListOf()

)