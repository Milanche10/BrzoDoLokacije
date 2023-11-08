package com.example.frontend.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

class ProfileOfOtherUserPostsFragment : Fragment(), OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    GoogleMap.OnMarkerClickListener{
    private lateinit var prefManager: SessionManager
    private lateinit var token: String
    //    lateinit var ProfilePostAdapter: ProfilePostAdapter
//    lateinit var gridLayoutManager: GridLayoutManager
//    var recyclerView: RecyclerView? = null
    lateinit var mapFragment: SupportMapFragment
    var locationSearch: EditText? = null
    var locationSearchView: SearchView? = null
    internal lateinit var mLastLocation: Location
    private var mMap: GoogleMap? = null
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    private var latLng: LatLng? = null
    internal var MyMarker: Marker? = null

    var sign_out_btn: Button? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View?{
        super.onCreate(savedInstanceState)
        var view : View = inflater.inflate(R.layout.activity_profile_of_other_user_posts_fragment, container, false) as ViewGroup

        var context: Context = view.getContext();

        prefManager = SessionManager(context)
        token = prefManager.getToken().toString()

        if(prefManager.isLogin() == false) {
            var intent: Intent = Intent(context, LoginActivity1::class.java)
            startActivity(intent)
        }

        val session = SessionManager(context)
        //System.out.println(session.getUserEmailFromPost().toString())

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<UserInfoItem> = apiInterface.getUserData(session.getUserEmailFromPost().toString())

        requestCall.enqueue(object : Callback<UserInfoItem> {

            override fun onResponse(
                call: Call<UserInfoItem>,
                response: Response<UserInfoItem>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    session.setUserIdFromPost(responseBody?.id)
                    //System.out.println(session.getId())
                }
                println("Uspesan zahtev")
            }

            override fun onFailure(call: Call<UserInfoItem>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })

        mapFragment = childFragmentManager.findFragmentById(R.id.mapsProfile) as SupportMapFragment

        mapFragment.getMapAsync(this)


        return view
    }
    fun odjaviSe(view: View){
        prefManager.removeData()
        var context: Context = view.getContext();
        var intent: Intent = Intent(context, LoginActivity1::class.java)
        startActivity(intent)
    }

    //  @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        /*TODO("Not yet implemented")*/
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
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
        mGoogleApiClient = context?.let {
            GoogleApiClient.Builder(it)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
        }
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
            LocationServices.getFusedLocationProviderClient(context)
        }
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            LocationServices.getFusedLocationProviderClient(context)
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


    fun getLocationDetails(lat: Double, lon: Double, id: Int) {
        var latitude = lat
        var longitude = lon
        latLng = LatLng(latitude, longitude)

        var geoCoder = Geocoder(context)
        var addresses: List<Address>? = null
        geoCoder = Geocoder(context, Locale.getDefault())
        try {
            addresses = geoCoder.getFromLocation(latitude, longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        MyMarker = mMap!!.addMarker(
            MarkerOptions().position(latLng!!)
                .title("" + id)
        )
        mMap!!.setOnMarkerClickListener(this)
        //  mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    fun getAllLocations()
    {
        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val session = SessionManager(context)
        System.out.println(session.getUserIdFromPost());
        var newUser = UserDataItem(session.getUserIdFromPost(),null,null,null,null);
        val requestCall: Call<List<PostDataItemLocation>> = apiInterface.getUserPosts(newUser)
        requestCall.enqueue(object : Callback<List<PostDataItemLocation>?> {
            override fun onResponse(
                call: Call<List<PostDataItemLocation>?>,
                response: Response<List<PostDataItemLocation>?>
            ) {
                val responseBody = response.body()!!

                var niz = emptyList<com.example.frontend.Location>().toMutableList()
                for (r in responseBody)
                {
                    r.location?.let { niz.add(it) }
                }
                var niz2 = emptyList<com.example.frontend.Location>().toMutableList()
                niz2 = niz.distinct() as MutableList<com.example.frontend.Location>
                for(n in niz2)
                {
                    println(n.name)
                    if(n.lat != null && n.lon != null)
                        getLocationDetails(n.lat, n.lon, n.id)
                }
                System.out.println(responseBody)
            }

            override fun onFailure(call: Call<List<PostDataItemLocation>?>, t: Throwable) {
                Log.d("ProfileActivity1", "onFailure:" + t.message)
            }
        })
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        var markerTitle = marker.title
        var intent: Intent = Intent(context, PostsByLocationAndUserOfOtherUser::class.java)
        intent.putExtra("id",markerTitle)
        startActivity(intent)

        return false
    }
}