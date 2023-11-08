package com.example.frontend.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.frontend.*
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class AddPostMapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    lateinit var mapFragment: SupportMapFragment
    var locationSearchView: SearchView? = null
    internal lateinit var mLastLocation: android.location.Location
    private var mMap: GoogleMap? = null
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    private var latLng: LatLng? = null
    var latMap: Double? = null
    var lonMap: Double? = null
    var MyMarker: Marker? = null
    var NazivLokacije: String? = null
    var requestCallLocation: Call<Location>? = null

    var submit_button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post_map)

        locationSearchView = findViewById(R.id.searchView)
        mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment;

        val session = SessionManager(this@AddPostMapActivity)

        locationSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query1: String?): Boolean {
                var location: String
                location = locationSearchView!!.query.toString()
                var addressList: List<Address>? = null

                mMap!!.clear()
                if (location != null || location != ""){

                    val geoCoder = Geocoder(this@AddPostMapActivity)
                    try {
                        addressList = geoCoder.getFromLocationName(location, 1)
                    }catch (e: IOException){
                        e.printStackTrace()
                    }

                    val address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    latMap = address.latitude
                    lonMap = address.longitude
                    mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
                 //   mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
                }

                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })

        submit_button = findViewById(R.id.locationBtn)
        submit_button?.setOnClickListener{
            if(locationSearchView!!.query.toString() != "" || NazivLokacije != null) {

                if(locationSearchView!!.query.toString() != "" && NazivLokacije == null) {
                    var newLocation = Location(
                        -1,
                        latMap,
                        lonMap,
                        locationSearchView!!.query.toString()
                    );
                    println(newLocation)
                    //prvi zahtev je dodavanje lokacije
                    val apiInterface: ApiInterface =
                        ServiceBuilder.buildService(ApiInterface::class.java)
                    requestCallLocation = apiInterface.addLocation(newLocation)
                }
                else if(locationSearchView!!.query.toString() == "" && NazivLokacije != null)
                {
                    var newLocation = Location(
                        -1,
                        latMap,
                        lonMap,
                        NazivLokacije
                    );
                    println(newLocation)
                    //prvi zahtev je dodavanje lokacije
                    val apiInterface: ApiInterface =
                        ServiceBuilder.buildService(ApiInterface::class.java)
                    requestCallLocation = apiInterface.addLocation(newLocation)
                }
                requestCallLocation?.enqueue(object : Callback<Location> {

                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(
                        call: Call<Location>,
                        response: Response<Location>
                    ) {
                        if (response.isSuccessful) {
                            var locationID: Int? = response.body()?.id
                            var locationName: String? = response.body()?.name
                            session.setLocationIdFromMap(locationID)
                            session.setLocationNameFromMap(locationName)
                            val intent = Intent(this@AddPostMapActivity, PostActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<Location>, t: Throwable) {
                        println("Neuspesan zahtev")
                        Toast.makeText(this@AddPostMapActivity, "Pokusajte ponovo.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else {
                Toast.makeText(this@AddPostMapActivity, "Izaberite lokaciju.", Toast.LENGTH_SHORT).show()
            }
        }

        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
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

        mMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener{
            override fun onMapClick(latLng: LatLng) {

                mMap!!.clear()
                MyMarker = mMap!!.addMarker(
                    MarkerOptions().position(latLng!!)
                )
                latMap = latLng.latitude
                lonMap = latLng.longitude
                var addressList: List<Address>? = null
                val geoCoder = Geocoder(this@AddPostMapActivity,  Locale.getDefault())
                try {
                    addressList = geoCoder.getFromLocation(latMap!!, lonMap!!, 1)
                    val obj: Address = addressList.get(0)
                    NazivLokacije = obj.getAddressLine(0)
                    println(NazivLokacije)
                    locationSearchView?.setQuery("",true)
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }

        })
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
        latMap = location.latitude
        lonMap = location.longitude
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

       // mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
       // mMap!!.moveCamera(CameraUpdateFactory.zoomTo(11f))

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
}