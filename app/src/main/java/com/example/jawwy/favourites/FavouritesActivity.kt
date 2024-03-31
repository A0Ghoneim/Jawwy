package com.example.jawwy.favourites

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jawwy.Map.MapActivity
import com.example.jawwy.connectivity.ConnectivityObserver
import com.example.jawwy.connectivity.NetworkConnectivityObserver
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherVieModelFactory
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherViewModel
import com.example.jawwy.databinding.ActivityFavouritesBinding
import com.example.jawwy.favourites.viewmodel.FavouriteViewModel
import com.example.jawwy.favourites.viewmodel.FavouriteViewModelFactory
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.db.WeatherLocalDataSource
import com.example.jawwy.model.repo.WeatherRepository
import com.example.jawwy.model.sharedprefrence.SharedPreferenceDatasource
import com.example.jawwy.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale

class FavouritesActivity : AppCompatActivity() {
    lateinit var binding: ActivityFavouritesBinding
    lateinit var factory: FavouriteViewModelFactory
    lateinit var viewModel: FavouriteViewModel
    lateinit var weatherViewMode: CurrentWeatherViewModel
    lateinit var favList: MutableList<JsonPojo>
    lateinit var connectivityObserver: ConnectivityObserver
    lateinit var adapter: FavouriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        if (isOnline(this)){
//            binding.floatingActionButton.visibility = View.VISIBLE
//        } else {
//            binding.floatingActionButton.visibility = View.GONE
//        }
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        connectivityObserver.getCurrentStatus().let {
            if (it == ConnectivityObserver.Status.Available) {
                binding.floatingActionButton.visibility = View.VISIBLE
            } else {
                binding.floatingActionButton.visibility = View.GONE
            }
        }
        lifecycleScope.launch {
            connectivityObserver.observe().collectLatest {
                if (it == ConnectivityObserver.Status.Available) {
                    binding.floatingActionButton.visibility = View.VISIBLE
                } else {
                    binding.floatingActionButton.visibility = View.GONE
                }
            }
        }



        factory = FavouriteViewModelFactory(
            WeatherRepository(
                WeatherRemoteDataSource.getInstance(this),
                WeatherLocalDataSource.getInstance(this),
                SharedPreferenceDatasource.getInstance(this)
            )
        )
        viewModel = ViewModelProvider(this, factory).get(FavouriteViewModel::class.java)


//        val wfactory = CurrentWeatherVieModelFactory(
//            WeatherRepository(
//                WeatherRemoteDataSource.getInstance(this),
//                WeatherLocalDataSource.getInstance(this),
//                SharedPreferenceDatasource.getInstance(this)
//            ), NetworkConnectivityObserver(applicationContext)
//        )
        val wfactory = CurrentWeatherVieModelFactory(
            WeatherRepository(
                WeatherRemoteDataSource.getInstance(this),
                WeatherLocalDataSource.getInstance(this),
                SharedPreferenceDatasource.getInstance(this)
            ), this
        )
        weatherViewMode = ViewModelProvider(this, wfactory).get(CurrentWeatherViewModel::class.java)
        viewModel.getAllWeather()



        favList = arrayListOf()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.favRecycler.layoutManager = linearLayoutManager
        adapter = FavouriteAdapter(viewModel, weatherViewMode, favList)
        binding.favRecycler.adapter = adapter

        lifecycleScope.launch {
            viewModel.weatherList.collectLatest { result ->
                when (result) {
                    is FavouriteApiState.Success -> {
                        favList = result.data as MutableList<JsonPojo>
//                       binding.progressBar2.visibility = View.GONE
                        adapter.updateList(favList)
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
//            if (isOnline(this)) {
//                startActivity(Intent(this, MapActivity::class.java))
//                finish()
//            }else{
//                recreate()
//            }
        }
    }
    private fun getCurrentLocale(context: Context): Locale {
        val configuration = context.resources.configuration
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            configuration.locales.get(0)
        } else {
            configuration.locale
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllWeather()
    }
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

}