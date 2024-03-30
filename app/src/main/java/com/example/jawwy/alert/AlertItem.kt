package com.example.jawwy.alert

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.jawwy.model.db.Converters
import java.time.LocalDateTime

@Entity
@TypeConverters(Converters::class)
data class AlertItem(@PrimaryKey val time:LocalDateTime, val city:String="", val country:String="",val lat:Double=0.0,val lon:Double=0.0)
