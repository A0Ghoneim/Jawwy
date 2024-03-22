package com.example.jawwy.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.jawwy.model.db.Converters
import com.google.gson.annotations.SerializedName

@Entity
@TypeConverters(Converters::class)
data class Daily (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") var id: Int = 0,
    @SerializedName("dt"         ) var dt        : Int?               = null,
    @SerializedName("sunrise"    ) var sunrise   : Int?               = null,
    @SerializedName("sunset"     ) var sunset    : Int?               = null,
    @SerializedName("moonrise"   ) var moonrise  : Int?               = null,
    @SerializedName("moonset"    ) var moonset   : Int?               = null,
    @SerializedName("moon_phase" ) var moonPhase : Double?            = null,
    @SerializedName("summary"    ) var summary   : String?            = null,
    @SerializedName("temp"       ) var temp      : Temp?              = Temp(),
    @SerializedName("feels_like" ) var feelsLike : FeelsLike?         = FeelsLike(),
    @SerializedName("pressure"   ) var pressure  : Int?               = null,
    @SerializedName("humidity"   ) var humidity  : Int?               = null,
    @SerializedName("dew_point"  ) var dewPoint  : Double?            = null,
    @SerializedName("wind_speed" ) var windSpeed : Double?            = null,
    @SerializedName("wind_deg"   ) var windDeg   : Int?               = null,
    @SerializedName("wind_gust"  ) var windGust  : Double?            = null,
    @SerializedName("weather"    ) var weather   : ArrayList<Weather> = arrayListOf(),
    @SerializedName("clouds"     ) var clouds    : Int?               = null,
    @SerializedName("pop"        ) var pop       : Double?               = null,
    @SerializedName("uvi"        ) var uvi       : Double?            = null
)

