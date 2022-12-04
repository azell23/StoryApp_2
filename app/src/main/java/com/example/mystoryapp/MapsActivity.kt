package com.example.mystoryapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.databinding.ActivityMapsBinding
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.datastore.Token
import com.example.mystoryapp.repository.AuthRepo
import com.example.mystoryapp.repository.MapsRepo
import com.example.mystoryapp.util.PreferenceFactory
import com.example.mystoryapp.viewmodel.Auth
import com.example.mystoryapp.viewmodel.StoryMaps
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var binding : ActivityMapsBinding
    private var listMap :ArrayList<LatLng>? = null
    private var listMapName: ArrayList<String>? = null
    private lateinit var authViewModel: Auth
    private val mapsViewModel: StoryMaps by viewModels {
        PreferenceFactory(authPreference,authrepository,this)
    }
    private lateinit var repository: MapsRepo
    private lateinit var authrepository: AuthRepo
    private lateinit var token: Token
    private lateinit var authPreference: AuthPreference
    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)

    }

    override fun onMapReady(p0: GoogleMap) {

        mMap = p0

        viewModel()
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true

        listMap = intent.getParcelableArrayListExtra(MainActivity.LIST_MAP)
        listMapName = intent.getStringArrayListExtra(MainActivity.LIST_MAP_NAME)

        getMyLocation()
        }

    private fun viewModel(){
        authPreference = AuthPreference(this)
        repository = MapsRepo()
        authrepository = AuthRepo()
        token = Token(authPreference)
        authViewModel = ViewModelProvider(this, PreferenceFactory(authPreference,authrepository, this))[Auth::class.java]
        token.getToken().observe(this){ token ->
            if (token != null){
                mapsViewModel.getStoryWithMaps("Bearer $token")
                mapsViewModel.getStory().observe(this){

                    it?.let {
                        for (i in it.listIterator()){
                            val latLng = LatLng(i.lat!!, i.lon!!)

                            mMap.addMarker(MarkerOptions().position(latLng).title(i.name)
                                .snippet(i.createdAt))
                        }
                    }
                }
            }
        }
    }
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            getMyLocation()
        }
    }
    private fun getMyLocation(){
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            mMap.isMyLocationEnabled = true
        } else {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.maps_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            R.id.satellite -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.normal -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.hybrid -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            R.id.terrain -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            } else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}