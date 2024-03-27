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
import com.example.jawwy.R
import com.example.jawwy.model.data.Daily
import com.example.jawwy.model.data.Hourly
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class DailyAdapter(var dataList:MutableList<Daily>, var symbol :String): RecyclerView.Adapter<DailyAdapter.MyViewHolder>() {
    lateinit var context : Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context=parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    fun updateList(newList: List<Daily>, newsymbol:String) {
        dataList.clear()
        dataList.addAll(newList)
        symbol=newsymbol
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentDaily: Daily = dataList[position]
        val maxd = currentDaily.temp?.max ?: 0.0
        val mind = currentDaily.temp?.min ?: 0.0
        val maxdegree:Int = maxd.toInt()
        val mindegree:Int = mind.toInt()
        val icon = currentDaily.weather[0].icon
        val link = "https://openweathermap.org/img/wn/$icon@2x.png"
        val dayname = getLocalDayFromUnixTimestamp(currentDaily.dt!!)
        val date = getDateFromUnixTimestamp(currentDaily.dt!!)
        val state = currentDaily.weather[0].main

        Log.i("DATE", "onDater: $date")
        holder.dayTV.text=dayname
        holder.stateTV.text=state
        holder.maxdegreeTV.text="$maxdegree$symbol"
        holder.mindegreeTV.text="$mindegree$symbol"


        Glide.with(context).load(link)
            .apply(RequestOptions().override(100, 100)).into(holder.iconView)
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val maxdegreeTV : TextView = itemView.findViewById(R.id.max_degree)
        val mindegreeTV : TextView = itemView.findViewById(R.id.min_degree)
        val dayTV : TextView = itemView.findViewById(R.id.day_text)
        val stateTV : TextView = itemView.findViewById(R.id.day_state)
        val iconView : ImageView = itemView.findViewById(R.id.day_icon)
    }
    fun getLocalDayFromUnixTimestamp(unixTimestamp: Int): String {
        // Convert Unix timestamp to LocalDateTime
        val dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(unixTimestamp.toLong()),
            ZoneId.systemDefault()
        )

        // Format LocalDateTime to get the local day
        val formatter = DateTimeFormatter.ofPattern("EEEE") // "EEEE" gives the full name of the day (e.g., "Monday")
        return dateTime.format(formatter)
    }
    fun getDateFromUnixTimestamp(unixTimestamp: Int): String {
        // Convert Unix timestamp to LocalDateTime
        val dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(unixTimestamp.toLong()),
            ZoneId.systemDefault()
        )

        // Format LocalDateTime to get the date
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Format the date as desired
        return dateTime.format(formatter)
    }

}
