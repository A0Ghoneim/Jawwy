package com.example.jawwy.alert

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.jawwy.MainActivity
import com.example.jawwy.R
import com.example.jawwy.alert.viewmodel.AlertViewModel
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherViewModel
import com.example.jawwy.favourites.viewholder.FavouriteViewModel
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.db.Converters

class AlertAdapter(private val viewModel: AlertViewModel, private var dataList: MutableList<AlertItem>) :
    RecyclerView.Adapter<AlertAdapter.MyViewHolder>() {
    lateinit var context : Context




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context=parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alert_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentAlert = dataList[position]
        holder.cardView.setOnLongClickListener {
          popDeleteMenu(it,currentAlert)
            true
        }
        holder.cardView.setOnClickListener {
            popDeleteMenu(it,currentAlert)

        }
        holder.cityTV.text=currentAlert.city
        holder.countryTV.text=currentAlert.country
        holder.dateTV.text= currentAlert.time.format(Converters.formatter)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    // Method to update the list
    fun updateList(newList: List<AlertItem>) {
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }
    fun popDeleteMenu(v:View,alertItem: AlertItem){
        // Create a PopupMenu and inflate the menu resource
        val popupMenu = PopupMenu(context,v)
        popupMenu.inflate(R.menu.delete_menu)
        Toast.makeText(context, "hjkhjk", Toast.LENGTH_SHORT).show()

        // Set up a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener {
            viewModel.deleteAlert(alertItem)
            true
        }

        // Show the PopupMenu
        popupMenu.show()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.alert_card)
        val cityTV : TextView = itemView.findViewById(R.id.city_name)
        val countryTV : TextView = itemView.findViewById(R.id.country_name)
        val dateTV : TextView = itemView.findViewById(R.id.date)
    }
}