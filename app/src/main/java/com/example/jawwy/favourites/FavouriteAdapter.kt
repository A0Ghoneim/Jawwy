package com.example.jawwy.favourites

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.jawwy.currentweather.view.MainActivity
import com.example.jawwy.R
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherViewModel
import com.example.jawwy.favourites.viewmodel.FavouriteViewModel
import com.example.jawwy.model.data.JsonPojo

class FavouriteAdapter(private val viewModel: FavouriteViewModel,private val weatherViewModel: CurrentWeatherViewModel, private var dataList: MutableList<JsonPojo>) :
    RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {
    lateinit var context : Context




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context=parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_list_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenWeather = dataList[position]
        holder.cardView.setOnLongClickListener {


            val  inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.refresh_menu_item_action_layout, null);

            val  start:CardView =view.findViewById(R.id.deleter)

            val  mypopupWindow =PopupWindow (view, 400, 210, true);

            start.setOnClickListener {
                Toast.makeText(context, "Location deleted", Toast.LENGTH_SHORT).show()
                viewModel.deletefav(currenWeather)
                mypopupWindow.dismiss()
            }

            mypopupWindow.showAsDropDown(it,700,-100);

            true
        }
        holder.cardView.setOnClickListener {
            weatherViewModel.putlat(currenWeather.lat!!)
            weatherViewModel.putLong(currenWeather.lon!!)
            weatherViewModel.putLocationSettings("manual")
            context.startActivity(Intent(context, MainActivity::class.java))

        }
        holder.cityTV.text=currenWeather.city
        holder.countryTV.text=currenWeather.country
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateList(newList: List<JsonPojo>) {
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView:CardView = itemView.findViewById(R.id.fav_card)
        val cityTV : TextView = itemView.findViewById(R.id.city_name)
        val countryTV : TextView = itemView.findViewById(R.id.country_name)
    }
}