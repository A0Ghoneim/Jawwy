package com.example.jawwy.favourites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jawwy.Map.MapActivity
import com.example.jawwy.R
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherVieModelFactory
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherViewModel
import com.example.jawwy.currentweather.viewmodel.WeatherApiState
import com.example.jawwy.databinding.ActivityFavouritesBinding
import com.example.jawwy.favourites.viewholder.FavouriteViewModel
import com.example.jawwy.favourites.viewholder.FavouriteViewModelFactory
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.db.WeatherLocalDataSource
import com.example.jawwy.model.repo.WeatherRepository
import com.example.jawwy.model.sharedprefrence.SharedPreferenceDatasource
import com.example.jawwy.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouritesActivity : AppCompatActivity() {
    lateinit var binding: ActivityFavouritesBinding
    lateinit var factory : FavouriteViewModelFactory
    lateinit var viewModel : FavouriteViewModel
    lateinit var weatherViewMode : CurrentWeatherViewModel
    lateinit var favList: MutableList<JsonPojo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = FavouriteViewModelFactory(
            WeatherRepository(
                WeatherRemoteDataSource,
                WeatherLocalDataSource.getInstance(this),
                SharedPreferenceDatasource.getInstance(this)
            )
        )
        viewModel = ViewModelProvider(this, factory).get(FavouriteViewModel::class.java)


        val wfactory = CurrentWeatherVieModelFactory(
            WeatherRepository(
                WeatherRemoteDataSource,
                WeatherLocalDataSource.getInstance(this),
                SharedPreferenceDatasource.getInstance(this)
            ), this
        )
        weatherViewMode = ViewModelProvider(this, wfactory).get(CurrentWeatherViewModel::class.java)
        viewModel.getAllWeather()



        favList = arrayListOf()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
        binding.favRecycler.layoutManager=linearLayoutManager
        val myadapter= FavouriteAdapter(viewModel,weatherViewMode,favList)
        binding.favRecycler.adapter=myadapter

        lifecycleScope.launch {
            viewModel.weatherList.collectLatest { result ->
                when (result) {
                    is FavouriteApiState.Success -> {
                        favList=result.data as MutableList<JsonPojo>
//                       binding.progressBar2.visibility = View.GONE
                        myadapter.updateList(favList)
                        myadapter.notifyDataSetChanged()
//                       binding.recyclerView.adapter=myadapter
                    }

                    is FavouriteApiState.Failure -> {
                        // binding.progressBar2.visibility = View.GONE
                    }

                    is FavouriteApiState.Loading -> {
                        //binding.progressBar2.visibility = View.VISIBLE }
                    }
                }
            }
        }

            binding.floatingActionButton.setOnClickListener {
                startActivity(Intent(this,MapActivity::class.java))
                finish()
            }
    }
}