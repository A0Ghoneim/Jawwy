package com.example.jawwy.alert.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jawwy.R
import com.example.jawwy.alert.AlertAdapter
import com.example.jawwy.alert.AlertItem
import com.example.jawwy.alert.AlertScheduler
import com.example.jawwy.alert.AlertSchedulerImp
import com.example.jawwy.alert.viewmodel.AlertApiState
import com.example.jawwy.alert.viewmodel.AlertViewModel
import com.example.jawwy.alert.viewmodel.AlertViewModelFactory
import com.example.jawwy.databinding.ActivityAlertsBinding
import com.example.jawwy.favourites.FavouriteAdapter
import com.example.jawwy.favourites.FavouriteApiState
import com.example.jawwy.favourites.viewholder.FavouriteViewModel
import com.example.jawwy.favourites.viewholder.FavouriteViewModelFactory
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.db.WeatherLocalDataSource
import com.example.jawwy.model.repo.WeatherRepository
import com.example.jawwy.model.sharedprefrence.SharedPreferenceDatasource
import com.example.jawwy.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class AlertsActivity : AppCompatActivity(), datelistener, timelistener {
    lateinit var date: LocalDateTime
    lateinit var timePicker: TimePickerFragment
    lateinit var scheduler:AlertScheduler
    lateinit var viewModel: AlertViewModel
    lateinit var binding:ActivityAlertsBinding
    lateinit var alertList:MutableList<AlertItem>
    var y = 0
    var m = 0
    var d = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAlertsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = AlertViewModelFactory(
            WeatherRepository(
                WeatherRemoteDataSource,
                WeatherLocalDataSource.getInstance(this),
                SharedPreferenceDatasource.getInstance(this)
            )
        )
        viewModel = ViewModelProvider(this, factory).get(AlertViewModel::class.java)

         scheduler = AlertSchedulerImp(this)



        viewModel.getAllAlerts()


        binding.addAlertFloatingActionButton.setOnClickListener {
            val newFragment = DatePickerFragment(this)
            newFragment.show(supportFragmentManager, "datePicker")
            timePicker = TimePickerFragment(this)
        }


        alertList = arrayListOf()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
        binding.alertRecycler.layoutManager=linearLayoutManager
        val myadapter= AlertAdapter(viewModel,alertList)
        binding.alertRecycler.adapter=myadapter

        lifecycleScope.launch {
            viewModel.alertList.collectLatest { result ->
                when (result) {
                    is AlertApiState.Success -> {

                     //   Log.i("TAG", "schedule: "+time.toString()+"  "+(time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000).minus(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()*1000))
                        alertList=result.data as MutableList<AlertItem>
                        //delete old
                        for (item in alertList){
                            if (item.time.atZone(ZoneId.systemDefault()).toEpochSecond().minus(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond())<0){
                                viewModel.deleteAlert(item)
                            }
                        }

                        //alertList=result.data as MutableList<AlertItem>
//                       binding.progressBar2.visibility = View.GONE
                        myadapter.updateList(alertList)
                        myadapter.notifyDataSetChanged()
//                       binding.recyclerView.adapter=myadapter
                    }

                    is AlertApiState.Failure -> {
                        // binding.progressBar2.visibility = View.GONE
                    }

                    is AlertApiState.Loading -> {
                        //binding.progressBar2.visibility = View.VISIBLE }
                    }
                }
            }
        }

    }

    override fun onDateArrive(year: Int, month: Int, day: Int) {
        y = year
        m = month+1
        d = day
        timePicker.show(supportFragmentManager, "timePicker")

    }

    override fun onTimeArrive(hourOfDay: Int, minute: Int) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        date = LocalDateTime.of(y, m, d, hourOfDay, minute)
        Log.i("TAG", "onDateSet: " + date.format(formatter))
        Toast.makeText(this@AlertsActivity, "" + date.format(formatter), Toast.LENGTH_SHORT).show()

        // add Ad
        val address = viewModel.getAddress(this)
        val city:String = address.locality ?:""
        val country:String = address.countryName ?:""
        val alert = AlertItem(LocalDateTime.of(y,m,d,hourOfDay,minute),city,country)
        viewModel.insertAlert(alert)

       // scheduler.schedule(LocalDateTime.now().plusSeconds(10))
        scheduler.schedule(LocalDateTime.of(y,m,d,hourOfDay,minute))
    }

    class TimePickerFragment(val timelistener: timelistener) : DialogFragment(),
        TimePickerDialog.OnTimeSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker.
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it.
            return TimePickerDialog(
                activity,
                this,
                hour,
                minute,
                DateFormat.is24HourFormat(activity)
            )
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            timelistener.onTimeArrive(hourOfDay, minute)
        }


    }

    class DatePickerFragment(val datelistener: datelistener) : DialogFragment(),
        DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker.
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

                val dtpicker = DatePickerDialog(requireContext(), this, year, month, day)
            dtpicker.datePicker.minDate=System.currentTimeMillis() - 1000
            // Create a new instance of DatePickerDialog and return it.
            return dtpicker

        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            Log.i("TAG", "onDateSet: year is $year")
            datelistener.onDateArrive(year, month, day)
        }
    }


}