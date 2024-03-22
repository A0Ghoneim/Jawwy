package com.example.jawwy.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.jawwy.model.db.Converters
import com.google.gson.annotations.SerializedName

@Entity
@TypeConverters(Converters::class)
data class Alerts (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") var id: Int = 0,
    @SerializedName("sender_name" ) var senderName  : String?           = null,
    @SerializedName("event"       ) var event       : String?           = null,
    @SerializedName("start"       ) var start       : Int?              = null,
    @SerializedName("end"         ) var end         : Int?              = null,
    @SerializedName("description" ) var description : String?           = null,
    @SerializedName("tags"        ) var tags        : ArrayList<String> = arrayListOf()

)
