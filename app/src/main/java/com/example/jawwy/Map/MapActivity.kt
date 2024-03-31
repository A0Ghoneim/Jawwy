package com.example.jawwy.Map

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Rect
import android.location.Address
import android.location.Geocoder
import android.location.GpsStatus
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.SearchView.OnQueryTextListener
import android.widget.SearchView.OnSuggestionListener
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.jawwy.currentweather.view.MainActivity
import com.example.jawwy.R
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherVieModelFactory
import com.example.jawwy.currentweather.viewmodel.CurrentWeatherViewModel
import com.example.jawwy.databinding.ActivityMapBinding
import com.example.jawwy.model.db.WeatherLocalDataSource
import com.example.jawwy.model.repo.WeatherRepository
import com.example.jawwy.model.searchdata.Features
import com.example.jawwy.model.sharedprefrence.SharedPreferenceDatasource
import com.example.jawwy.network.WeatherRemoteDataSource
import com.example.jawwy.search.ViewModel.FeatureApiState
import com.example.jawwy.search.ViewModel.SearchViewModel
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.config.Configuration.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.Locale


private const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
private val sAutocompleteColNames = arrayOf(
    BaseColumns._ID,  // necessary for adapter
    SearchManager.SUGGEST_COLUMN_TEXT_1 // the full search term
)
class MapActivity : AppCompatActivity(), MapListener,MapEventsReceiver, GpsStatus.Listener {
    lateinit var mMap: MapView
    lateinit var controller: IMapController;
    lateinit var mMyLocationOverlay: MyLocationNewOverlay
    lateinit var marker: Marker
    lateinit var geocoder:Geocoder
    lateinit var act:MapActivity
    lateinit var searchList : ArrayList<Features>
    lateinit var weatherViewModel : CurrentWeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = SearchViewModel(WeatherRepository(WeatherRemoteDataSource.getInstance(this),WeatherLocalDataSource.getInstance(this),SharedPreferenceDatasource.getInstance(this)))

//        val factory = CurrentWeatherVieModelFactory(
//            WeatherRepository(
//                WeatherRemoteDataSource.getInstance(this),
//                WeatherLocalDataSource.getInstance(this),
//                SharedPreferenceDatasource.getInstance(this)
//            ), NetworkConnectivityObserver(applicationContext)
//        )
        val factory = CurrentWeatherVieModelFactory(
            WeatherRepository(
                WeatherRemoteDataSource.getInstance(this),
                WeatherLocalDataSource.getInstance(this),
                SharedPreferenceDatasource.getInstance(this)
            ), this
        )
        weatherViewModel = ViewModelProvider(this, factory).get(CurrentWeatherViewModel::class.java)
        lifecycleScope.launch {
            viewModel.featureList.collectLatest { result ->
                when (result) {
                    is FeatureApiState.Success -> {
                        val cursor = MatrixCursor(sAutocompleteColNames)

                        Log.i("TAG", "onQueryTextChange: "+result.data.size)
                        // get your search terms from the server here, ex:

                        // get your search terms from the server here, ex:
                        var terms = mutableListOf<Features>()
                        terms = result.data as MutableList<Features>

                        searchList=terms as ArrayList<Features>

                        // parse your search terms into the MatrixCursor

                        // parse your search terms into the MatrixCursor
                        for (index in 0 until result.data.size) {
                            //if (terms.get(index).properties?.city!=null) {
                            Log.i("TAG", "onQueryTextChange"+terms.get(index).properties?.name)
                            val term = terms.get(index).properties?.name + " , " + terms.get(index).properties?.country
                            val row = arrayOf<Any>(index, term!!)
                            cursor.addRow(row)
                            // }
                        }

                        binding.searchView.getSuggestionsAdapter().changeCursor(cursor);
                    }

                    is FeatureApiState.Failure -> {
                    }

                    is FeatureApiState.Loading -> {
                    }
                }
            }
        }
        binding.searchView.setSuggestionsAdapter(
            SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                null,
                arrayOf<String>(SearchManager.SUGGEST_COLUMN_TEXT_1),
                intArrayOf(android.R.id.text1)
            )
        )
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length!! >= 3) {
                    viewModel.search(newText, 5)
                }
                return true
            }
        })
        binding.searchView.setOnSuggestionListener(object : OnSuggestionListener{
            @SuppressLint("Range")
            override fun onSuggestionSelect(position: Int): Boolean {
                for (i in searchList){
                    Log.d("After", "on ${i.properties?.name}")
                }
                val cursor = binding.searchView.suggestionsAdapter.getItem(position) as Cursor
                val term = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                Log.d("TAG", "onSuggestionSelect: "+term)
                cursor.close()
                val  feature = searchList[term]
                val coordinates =   feature.geometry?.coordinates
                Log.d("TAG", "onSuggestionSelect: "+coordinates?.get(0) + coordinates?.get(1))
                val p = GeoPoint(coordinates?.get(1)!!,coordinates?.get(0)!!)
                hoverTo(p)
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                return onSuggestionSelect(position);
            }

        })



        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )
        geocoder = Geocoder(this, Locale.getDefault())
        mMap = binding.osmmap
        marker =  Marker(mMap)
        // marker.icon=getDrawable(R.drawable.locationfill)
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.mapCenter
        mMap.setMultiTouchControls(true)
        mMap.getLocalVisibleRect(Rect())

        act = this

        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mMap)
        controller = mMap.controller

        mMyLocationOverlay.enableMyLocation()
        mMyLocationOverlay.enableFollowLocation()
        mMyLocationOverlay.isDrawAccuracyEnabled = true
        mMyLocationOverlay.runOnFirstFix {
            runOnUiThread {
                controller.setCenter(mMyLocationOverlay.myLocation);
                controller.animateTo(mMyLocationOverlay.myLocation)
            }
        }
        // val mapPoint = GeoPoint(latitude, longitude)

        controller.setZoom(4.0)

//        Log.e("TAG", "onCreate:in ${controller.zoomIn()}")
//        Log.e("TAG", "onCreate: out  ${controller.zoomOut()}")

        // controller.animateTo(mapPoint)
        mMap.overlays.add(mMyLocationOverlay)

        mMap.addMapListener(this)

        mMap.overlays.add(MapEventsOverlay(this))

    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        // event?.source?.getMapCenter()
//        Log.e("TAG", "onCreate:la ${event?.source?.getMapCenter()?.latitude}")
//        Log.e("TAG", "onCreate:lo ${event?.source?.getMapCenter()?.longitude}")
        //  Log.e("TAG", "onScroll   x: ${event?.x}  y: ${event?.y}", )
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        //  event?.zoomLevel?.let { controller.setZoom(it) }


//        Log.e("TAG", "onZoom zoom level: ${event?.zoomLevel}   source:  ${event?.source}")
        return false;
    }

    override fun onGpsStatusChanged(event: Int) {


        TODO("Not yet implemented")
    }

    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {

        hoverTo(p)

        return true
    }
    override fun longPressHelper(p: GeoPoint?): Boolean {
        return true
    }

    fun hoverTo(p:GeoPoint?):Unit {
        controller.animateTo(p)
        marker.position = p
        mMap.overlays.add(marker)

        //////////////////////


        var bottomSheetDialog = BottomSheetDialog(act);
        val view1 = LayoutInflater.from(act).inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();


        val dismissBtn: Button = view1.findViewById(R.id.dismiss);
        val confirmBtn: Button = view1.findViewById(R.id.confirm)
        val addrtext: TextView = view1.findViewById(R.id.textViewaddr)

        val addresses: List<Address>?
        if (p != null) {
            addresses = geocoder.getFromLocation(
                p.latitude,
                p.longitude,
                1
            )
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            val address =
                addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            val city = addresses!![0].locality
            val state = addresses!![0].adminArea
            val country = addresses!![0].countryName
            val postalCode = addresses!![0].postalCode
            val knownName = addresses!![0].featureName // Only if available else return NULL

            addrtext.text = address


            dismissBtn.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            confirmBtn.setOnClickListener {
                Log.i("TAG", "hoverTo: "+p.latitude)
                // change the location Shared preference
                weatherViewModel.putlat(p.latitude)
                weatherViewModel.putLong(p.longitude)
                weatherViewModel.putLocationSettings("manual")

                weatherViewModel.fetchWeather()

                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
        }



    }
}