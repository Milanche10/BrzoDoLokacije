package com.example.frontend

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.frontend.activities.AddPostMapActivity
import com.example.frontend.models.Follow
import com.example.frontend.models.SocialPost
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
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.util.*
import kotlin.properties.Delegates


class PostActivity : AppCompatActivity(){

    var submit_button: Button? = null
    var izaberislike_button: ImageView? = null
    var odustani_button: Button? = null
    var vratiSeNazad: CircleImageView? = null
    var description: EditText? = null

    private var pickedImages: MutableList<Uri> by Delegates.observable(mutableListOf<Uri>()){ _, _, _ ->
        updatePage()
    }

    lateinit var dotsLayout: LinearLayout
    lateinit var mPager: ViewPager
    lateinit var dots: Array<ImageView>
    lateinit var adapter: PageViewAddNewPost

    var currentPage: Int = 0
    lateinit var timer: Timer
    val DELAY_MS: Long = 5000
    val PERIOD_MS: Long = 5000


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val session = SessionManager(this@PostActivity)

        findViewById<TextView>(R.id.locationName).text = "Lokacija: " + session.getLocationNameFromMap().toString()

        mPager = findViewById<View>(R.id.pager) as ViewPager
        adapter = PageViewAddNewPost(this, pickedImages)

        mPager.adapter = adapter
        dotsLayout = findViewById(R.id.dotsLayout)

        createDots(0)
        updatePage()

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (pickedImages.isEmpty()) {
            mPager.visibility = View.GONE
        }

        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //end
            }

            override fun onPageSelected(position: Int) {
                createDots(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                //end
            }
        })

        var locationIDFromMap: Int = -1
        if(session.getLocationIdFromMap() != -1) {
            locationIDFromMap = session.getLocationIdFromMap()!!
            //println(locationIDFromMap)
        }

        description = findViewById(R.id.postText1)
        izaberislike_button = findViewById(R.id.button)
        odustani_button = findViewById(R.id.goBack_btn)
        vratiSeNazad = findViewById(R.id.back)

        submit_button = findViewById(R.id.postBtn)
        submit_button?.isEnabled = true
        submit_button?.isClickable = true
        submit_button?.setOnClickListener {
            submit_button?.isEnabled = false
            submit_button?.isClickable = false
            if (locationIDFromMap != -1) {

                var newLoc = Location(locationIDFromMap!!, 8.2, 3.9, null);
                var appUser = UserDataItem(session.getId(), null, null, null, null);
                var name: String = "Post" + locationIDFromMap;
                val current = LocalDate.now()
                //println("Current time")
                //println(current)
                var current1: String = "2016-08-14T12:17:47.720Z"
                var newSocialPost = SocialPost(-1, name, description!!.text.toString(), current1, newLoc, appUser, null, null, null);

                val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
                val requestCall: Call<SocialPost> = apiInterface.addSocialPost(newSocialPost)
                requestCall.enqueue(object : Callback<SocialPost> {
                    override fun onResponse(
                        call: Call<SocialPost>,
                        response: Response<SocialPost>
                    ) {
                        if (response.code() == 200) {
                            var socialPostId: Int? = response.body()?.id

                            var otherImagesFiles = mutableListOf<File>()
                            for (i in 0 until pickedImages.size) {
                                otherImagesFiles.add(
                                    File(getRealPathFromUri(pickedImages[i])!!)
                                )
                            }
                            var loadingDialog: LoadingDialog = LoadingDialog(this@PostActivity)
                            if(pickedImages.size != 0){

                                for (i in 0 until pickedImages.size) {

                                    if(i == 0){
                                        Toast.makeText(this@PostActivity, "Ucitavanje slika je u toku.", Toast.LENGTH_SHORT).show()
                                    }

                                    uploadPhotos(socialPostId, otherImagesFiles[i])

                                    loadingDialog.startLoadingDialog()
                                    Handler().postDelayed({ loadingDialog.dismissDialog() }, 9000)
                                }

                                var intent = Intent(this@PostActivity, HomeActivity::class.java)
                                startActivity(intent)
                            }
                            else {
                                Toast.makeText(this@PostActivity, "Unesite fotografije.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<SocialPost>, t: Throwable) {
                        println("Neuspesan zahtev")
                    }
                })
            }
            else {
                Toast.makeText(this, "Unesite sve podatke.", Toast.LENGTH_SHORT).show()
            }
        }

        izaberislike_button?.setOnClickListener {
            verifyStoragePermissions(this)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, 101)
        }

        odustani_button?.setOnClickListener {
            var lok = session.getLocationIdFromMap()
            izbrisiLokaciju(lok?.toLong())
            var intent = Intent(this@PostActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        vratiSeNazad?.setOnClickListener {
            var lok = session.getLocationIdFromMap()
            izbrisiLokaciju(lok?.toLong())
            var intent = Intent(this@PostActivity, AddPostMapActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getRealPathFromUri(contentUri: Uri): String? {
        var result: String?

        val cursor = this.contentResolver?.query(contentUri, null, null, null, null)
        if (cursor == null) {
            result = contentUri.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }

        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 101 && data != null && data.data != null && data.clipData == null) {
            pickedImages.add(0, data.data!!)
            adapter = PageViewAddNewPost(this, pickedImages)
            mPager.adapter = adapter
            mPager.visibility = View.VISIBLE
        } else if (resultCode == Activity.RESULT_OK && requestCode == 101 && data != null && data.clipData != null) {

            var count = data.clipData?.itemCount

            if (count == null) count = 0

            for (i in 0 until count) {
                var localImageUri: Uri? = data.clipData?.getItemAt(i)?.uri
                if (localImageUri != null) {
                    pickedImages.add(i, localImageUri)
                }
            }
            adapter = PageViewAddNewPost(this, pickedImages)
            mPager.adapter = adapter
            mPager.visibility = View.VISIBLE

        }
    }

    fun createDots(position: Int) {
        if (dotsLayout != null) {
            dotsLayout.removeAllViews()
        }
        dots = Array(pickedImages.size) { ImageView(this) }
        for (i in 0 until pickedImages.size) {
            dots[i] = ImageView(this)
            if (i == position) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots))
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dots))
            }

            var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(4, 0, 4, 0)
            dotsLayout.addView(dots[i], params)
        }
    }

    private fun verifyStoragePermissions(activity: Activity) {
        val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val REQUEST_EXTERNAL_STORAGE = 1
        // check if we have write permission
        val permission: Int = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    private fun updatePage() {
        var handler = Handler()
        val update = Runnable {
            if (currentPage == pickedImages.size) {
                currentPage = 0
            }
            mPager.setCurrentItem(currentPage++, true)
        }
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    fun goToAddLocation(view: View?) {
        val intent = Intent(this@PostActivity, AddPostMapActivity::class.java)
        startActivity(intent)
    }

     fun uploadPhotos(socialPostId: Int?, otherImagesFiles: File) {

        var requestBody: RequestBody
        val mapRequestBody = LinkedHashMap<String, RequestBody>()
        val arrBody: MutableList<MultipartBody.Part> = ArrayList()

        var body: MultipartBody.Part? = null
         Log.d(TAG, "file===" + otherImagesFiles.getName());

         requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), otherImagesFiles)
         mapRequestBody["file\"; filename=\"" + otherImagesFiles.getName()] = requestBody
         mapRequestBody["test"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "file")
         body = MultipartBody.Part.createFormData("fileName", otherImagesFiles.getName(), requestBody)
         arrBody.add(body)

         val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
         val requestCall: Call<Image?>? = apiInterface.uploadFile(mapRequestBody, socialPostId)

         if (requestCall != null) {
             requestCall.enqueue(object : Callback<Image?> {
                 override fun onResponse(
                     call: Call<Image?>,
                     response: Response<Image?>
                 ) {
                     if (response.isSuccessful) {

                     }
                 }
                 override fun onFailure(call: Call<Image?>, t: Throwable) {
                     Log.e(TAG + "Err", t.message!!)
                 }
             })
         } else {
         }
     }
/*
    fun uploadPhotos(socialPostId: Int?) {
        var otherImagesFiles = mutableListOf<File>()
        for (i in 0 until pickedImages.size) {
            otherImagesFiles.add(
                File(getRealPathFromUri(pickedImages[i])!!)
            )
        }

        var requestBody: RequestBody
        val mapRequestBody = LinkedHashMap<String, RequestBody>()
        val arrBody: MutableList<MultipartBody.Part> = ArrayList()

        if(pickedImages.size != 0){

            for (i in 0 until pickedImages.size) {
                var body: MultipartBody.Part? = null
                Log.d(TAG, "file===" + otherImagesFiles[i].getName());

                //println(otherImagesFiles[i].getName())

                requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), otherImagesFiles[i])
                mapRequestBody["file\"; filename=\"" + otherImagesFiles[i].getName()] = requestBody
                mapRequestBody["test"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "file")
                body = MultipartBody.Part.createFormData("fileName", otherImagesFiles[i].getName(), requestBody)
                arrBody.add(body)

                val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
                val requestCall: Call<Image?>? = apiInterface.uploadFile(mapRequestBody, socialPostId)
                var loadingDialog: LoadingDialog = LoadingDialog(this@PostActivity)
                loadingDialog.startLoadingDialog()
                Handler().postDelayed({ loadingDialog.dismissDialog() }, 9000)

                if (requestCall != null) {
                    requestCall.enqueue(object : Callback<Image?> {
                        override fun onResponse(
                            call: Call<Image?>,
                            response: Response<Image?>
                        ) {
                            if (response.isSuccessful) {
                                    //var intent = Intent(this@PostActivity, HomeActivity::class.java)
                                    //startActivity(intent)

                            }
                        }
                        override fun onFailure(call: Call<Image?>, t: Throwable) {
                            Log.e(TAG + "Err", t.message!!)
                        }
                    })
                } else {
                }
                println("Uspesan zahtev")
            }
        }
        else {
            Toast.makeText(this@PostActivity, "Unesite fotografije.", Toast.LENGTH_SHORT).show()
        }
    }
*/
    fun izbrisiLokaciju(locationID: Long?)
    {
        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<String> = apiInterface.deleteLocation(locationID)

        requestCall.enqueue(object : Callback<String> {

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {

                    println("Uspesan zahtev")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })
    }

    fun goToAddPost(view: View?) {

    }

}