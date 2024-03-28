package com.example.jawwy.favourites

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.jawwy.MainActivity
import com.example.jawwy.R
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherViewModel
import com.example.jawwy.favourites.viewholder.FavouriteViewModel
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
        val address = viewModel.getAddress(currenWeather,context)
        holder.cardView.setOnLongClickListener {

            //meal=this.meal;
            // Create a PopupMenu and inflate the menu resource
        /*    val popupMenu = PopupMenu(context,it,Gravity.RIGHT)
            popupMenu.inflate(R.menu.delete_menu)
            popupMenu.setForceShowIcon(true)
            Toast.makeText(context, "hjkhjk", Toast.LENGTH_SHORT).show()

            // Set up a listener for menu item clicks
            popupMenu.setOnMenuItemClickListener {
                viewModel.deletefav(currenWeather)
                true
            }

            // Show the PopupMenu
            popupMenu.show()*/

            val  inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.refresh_menu_item_action_layout, null);

           val  start:CardView =view.findViewById(R.id.deleter)

            val  mypopupWindow =PopupWindow (view, 400, 210, true);

            start.setOnClickListener {
                Toast.makeText(context, "action", Toast.LENGTH_SHORT).show()
                viewModel.deletefav(currenWeather)
                mypopupWindow.dismiss()
            }




           /* mypopupWindow.contentView.setOnClickListener{
                Toast.makeText(context, "dismisser", Toast.LENGTH_SHORT).show()

            }*/


            mypopupWindow.showAsDropDown(it,700,-100);

            true
        }
        holder.cardView.setOnClickListener {
            weatherViewModel.putlat(currenWeather.lat!!)
            weatherViewModel.putLong(currenWeather.lon!!)
            weatherViewModel.putLocationSettings("manual")
            context.startActivity(Intent(context,MainActivity::class.java))

        }
        holder.cityTV.text=address.locality
        holder.countryTV.text=address.countryName
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    // Method to update the list
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