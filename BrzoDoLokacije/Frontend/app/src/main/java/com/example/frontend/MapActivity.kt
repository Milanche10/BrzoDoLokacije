package com.example.frontend

import android.Manifest
import android.content.Intent
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.frontend.activities.AddPostMapActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    GoogleMap.OnMarkerClickListener {

    lateinit var mapFragment: SupportMapFragment
    var locationSearch: EditText? = null
    var locationSearchView: SearchView? = null
    internal lateinit var mLastLocation: android.location.Location
    private var mMap: GoogleMap? = null
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    private var latLng: LatLng? = null
    internal var MyMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.getMenu().findItem(R.id.search).setChecked(true);

        navView.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.home -> startActivity(
                    Intent(
                        this,
                        HomeActivity::class.java
                    )
                )
                R.id.profile -> startActivity(
                    Intent(
                        this,
                        ProfileActivity1::class.java
                    )
                )
                R.id.favorite -> startActivity(
                    Intent(
                        this,
                        LikespageActivity::class.java
                    )
                )
            }
            true
        }

       // locationSearch = findViewById(R.id.et_search)
        locationSearchView = findViewById(R.id.searchView)
        mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment;

        locationSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query1: String?): Boolean {
                var location: String
                location = locationSearchView!!.query.toString()
                var addressList: List<Address>? = null

                if (location != null || location != ""){

                    val geoCoder = Geocoder(this@MapActivity)
                    try {
                        addressList = geoCoder.getFromLocationName(location, 1)
                    }catch (e: IOException){
                        e.printStackTrace()
                    }

                    val address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
                }

                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
               return false
            }
        })

        mapFragment.getMapAsync(this)

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        /*TODO("Not yet implemented")*/
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ){
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        }else{
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }

        getAllLocations()
    }

    protected fun buildGoogleApiClient(){
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(location: android.location.Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null){
            mCurrLocationMarker!!.remove()
        }

        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(11f))

        if (mGoogleApiClient != null){
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    fun searchLocation(view: View){

        var location: String
        location = locationSearch?.text.toString().trim()
        var addressList: List<Address>? = null

        if (location == null || location == ""){
            Toast.makeText(this, "provide location", Toast.LENGTH_SHORT).show()
        }else{
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location, 1)
            }catch (e: IOException){
                e.printStackTrace()
            }

            val address = addressList!![0]
            val latLng = LatLng(address.latitude, address.longitude)
            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
        }
    }

    fun getLocationDetails(lat: Double, lon: Double, id: Int, name: String) {

        var latitude = lat
        var longitude = lon
        latLng = LatLng(latitude, longitude)

        var geoCoder = Geocoder(this)
        var addresses: List<Address>? = null
        geoCoder = Geocoder(this, Locale.getDefault())

        var address: String? = null
        var city: String? = null
        var state: String? = null
        var country: String? = null
        var knonName: String? = null
        try {
            addresses = geoCoder.getFromLocation(latitude, longitude, 1)
//            address = addresses[0].getAddressLine(0)
//            city = addresses[0].locality
//            state = addresses[0].adminArea
//            country = addresses[0].countryName
//            knonName = addresses[0].featureName
        } catch (e: IOException) {
            e.printStackTrace()
        }
        MyMarker = mMap!!.addMarker(
            MarkerOptions().position(latLng!!)
              //  .title(name)
                .snippet(id.toString())
        )

        mMap!!.setOnMarkerClickListener(this)
      //  mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    fun getAllLocations()
    {
        val apiInterface: ApiInterface =
            ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<List<LocationMap>> = apiInterface.getAllLocations()

        requestCall.enqueue(object : Callback<List<LocationMap>?> {
            override fun onResponse(
                call: Call<List<LocationMap>?>,
                response: Response<List<LocationMap>?>
            ) {
                val responseBody = response.body()!!
                println(responseBody)
                for(myData in responseBody)
                {
                    getLocationDetails(myData.lat, myData.lon, myData.id, myData.name)
                }
            }

            override fun onFailure(call: Call<List<LocationMap>?>, t: Throwable) {
                Log.d("MapActivity", "onFailure:" + t.message)
            }
        })
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        var markerTitle = marker.snippet
        var intent: Intent = Intent(this, PostsByLocation::class.java)
        intent.putExtra("id",markerTitle)
        startActivity(intent)

        return false
    }

    fun goToAddPost(view: View?) {
        val intent = Intent(this@MapActivity, AddPostMapActivity::class.java)
        startActivity(intent)
    }

}