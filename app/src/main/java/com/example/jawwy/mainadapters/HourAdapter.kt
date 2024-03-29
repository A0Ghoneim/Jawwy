package com.example.jawwy.mainadapters

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jawwy.CELSIUS
import com.example.jawwy.FAHRENHEIT
import com.example.jawwy.MILE
import com.example.jawwy.R
import com.example.jawwy.UnitConverter
import com.example.jawwy.alert.AlertItem
import com.example.jawwy.model.data.Hourly
import java.util.Date
import java.util.Locale

class HourAdapter(var dataList:MutableList<Hourly>, var symbol :String, var speedUnit:String):RecyclerView.Adapter<HourAdapter.MyViewHolder>() {
    lateinit var context : Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context=parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hour_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    fun updateList(newList: List<Hourly>,newsymbol:String,newSpeedSymbol:String) {
        dataList.clear()
        dataList.addAll(newList)
        symbol=newsymbol
        speedUnit=newSpeedSymbol
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentHour:Hourly = dataList[position]
        var d = currentHour.temp ?: 0.0
        if (symbol== CELSIUS){
            d= UnitConverter.kelvinToCelsius(d)
        }else if (symbol == FAHRENHEIT){
            d= UnitConverter.kelvinToFahrenheit(d)
        }
        val degree:Int = d.toInt()
        var wind = currentHour.windSpeed ?: 0.0
        if (speedUnit== MILE){
            wind = UnitConverter.meterPerSecondToMilesPerHour(wind)
        }
        val icon = currentHour.weather[0].icon
        val link = "https://openweathermap.org/img/wn/$icon@2x.png"
        var localHour = getLocalHourFromUnixTimestamp(currentHour.dt!!)
        var dayNNightStamp = "AM"
        if (localHour>12){
            localHour -= 12
            dayNNightStamp ="PM"
        }
        else if(localHour==12){dayNNightStamp = "PM"}
        else if(localHour==0){localHour=12}
        Log.i("Hour", "onBindViewHolder: $localHour")
        holder.degreeTV.text="$degree$symbol"
        holder.timeTV.text="$localHour$dayNNightStamp"
        holder.windTV.text="$wind$speedUnit"
        Glide.with(context).load(link)
            .apply(RequestOptions().override(100, 100)).into(holder.iconView)
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.hour_card)
        val degreeTV : TextView = itemView.findViewById(R.id.hour_degree)
        val timeTV : TextView = itemView.findViewById(R.id.hour_time)
        val windTV :TextView = itemView.findViewById(R.id.wind_hour)
        val iconView : ImageView = itemView.findViewById(R.id.hour_imageView)
    }
    fun getLocalHourFromUnixTimestamp(gmtUnixTimestamp: Int): Int {
        // Create a Date object from the Unix timestamp
        val date = Date(gmtUnixTimestamp * 1000L)

        // Get the default time zone
        val timeZone = TimeZone.getDefault()

        // Create a SimpleDateFormat object with the desired format
        val sdf = SimpleDateFormat("HH", Locale.getDefault())

        // Set the time zone of the SimpleDateFormat object to the local time zone
        sdf.timeZone = timeZone

        // Format the date to get the hour in the local time zone
        return sdf.format(date).toInt()
    }

}