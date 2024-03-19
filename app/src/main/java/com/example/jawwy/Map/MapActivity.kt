package com.example.jawwy.Map

import android.app.SearchManager
import android.content.Intent
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
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jawwy.MainActivity
import com.example.jawwy.R
import com.example.jawwy.databinding.ActivityMapBinding
import com.example.jawwy.model.WeatherRepository
import com.example.jawwy.model.searchdata.Features
import com.example.jawwy.network.WeatherRemoteDataSource
import com.example.jawwy.search.ViewModel.ApiState
import com.example.jawwy.search.ViewModel.SearchViewModel
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONArray
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = SearchViewModel(WeatherRepository(WeatherRemoteDataSource()))


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
                lifecycleScope.launch {
                    viewModel.featureList.collectLatest { result ->
                        when (result) {
                            is ApiState.Success -> {
                                val cursor = MatrixCursor(sAutocompleteColNames)

                                Log.i("TAG", "onQueryTextChange: "+result.data.size)
                                // get your search terms from the server here, ex:

                                // get your search terms from the server here, ex:
                                var terms = mutableListOf<Features>()
                                 terms = result.data as MutableList<Features>

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

                            is ApiState.Failure -> {
                            }

                            is ApiState.Loading -> {
                            }
                        }
                    }
                }
                return true
            }
        })



        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )
        geocoder = Geocoder(this, Locale.getDefault())
        mMap = binding.osmmap
        marker =  Marker(mMap)
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
        controller.animateTo(p)
        marker.position = p
        mMap.overlays.add(marker)

        //////////////////////



        var bottomSheetDialog = BottomSheetDialog(act);
        val view1 = LayoutInflater.from(act).inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();


        val dismissBtn:Button = view1.findViewById(R.id.dismiss);
        val confirmBtn:Button = view1.findViewById(R.id.confirm)
        val addrtext:TextView = view1.findViewById(R.id.textViewaddr)

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

            addrtext.text =address

        }

        dismissBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        confirmBtn.setOnClickListener {
            val intent = Intent(act,MainActivity::class.java)
            if (p != null) {
                intent.putExtra("lat",p.latitude)
                intent.putExtra("long",p.longitude)
            }
            startActivity(intent)


        }



      return true
    }
    override fun longPressHelper(p: GeoPoint?): Boolean {
        return true
    }

}