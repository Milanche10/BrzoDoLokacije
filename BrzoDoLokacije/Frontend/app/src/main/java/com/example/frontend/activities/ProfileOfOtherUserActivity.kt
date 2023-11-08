package com.example.frontend.activities
import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.example.frontend.models.Follow
import androidx.fragment.app.Fragment
import com.example.frontend.*
import com.example.frontend.adapters.ProfileOfOtherUserAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList
import java.util.LinkedHashMap

class ProfileOfOtherUserActivity : AppCompatActivity() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    private lateinit var circleIV: CircleImageView;
    private lateinit var button: CircleImageView;

    var follow_btn: Button? = null
    var unfollow_btn: Button? = null
    var edit_profile_btn: Button? = null
    lateinit var fragment: Fragment

    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_of_other_user)

        findViewById<TextView>(R.id.edit_profile_btn).isVisible = false;

        verifyStoragePermissions(this)

        fragment =  ProfileInfoFragmentOfOtherUser()

        supportFragmentManager.beginTransaction()
            .replace(R.id.view_pager,fragment)
            .commit()

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        //navView.getMenu().findItem(R.id.profile).setChecked(true);

        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> startActivity(
                    Intent(
                        this,
                        HomeActivity::class.java
                    )
                )
                R.id.search -> startActivity(
                    Intent(
                        this,
                        MapActivity::class.java
                    )
                )
                R.id.profile -> startActivity(
                    Intent(
                        this,
                        ProfileActivity1::class.java
                    )
                )
            }
            true
        }

        tabLayout = findViewById<TabLayout>(R.id.tablayout)
        viewPager = findViewById<ViewPager>(R.id.view_pager)

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Objave"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Lokacije"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = ProfileOfOtherUserAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        findViewById<TextView>(R.id.idKorisnika).text = "-1"
        findViewById<TextView>(R.id.numOfFollowing).text = "0"
        findViewById<TextView>(R.id.numOfFollowers).text = "0"
        findViewById<TextView>(R.id.numOfLocations).text = "0"

        button = findViewById(R.id.addPhoto)
        circleIV = findViewById(R.id.imageView)

        var context: Context = applicationContext;
        val session = SessionManager(context)

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<UserInfoItem> = apiInterface.getUserData(session.getUserEmailFromPost().toString())

        requestCall.enqueue(object : Callback<UserInfoItem> {

            override fun onResponse(
                call: Call<UserInfoItem>,
                response: Response<UserInfoItem>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()
                    findViewById<TextView>(R.id.firstname).text = responseBody?.firstName + " " + responseBody?.lastName
                    findViewById<TextView>(R.id.emailuser).text = responseBody?.email
                    findViewById<TextView>(R.id.idKorisnika).text = responseBody?.id.toString()
                    //println(findViewById<TextView>(R.id.idKorisnika).text.toString())
                    var trId = findViewById<TextView>(R.id.idKorisnika).text.toString()
                    println("----------------------------------------------------------------")
                    println(trId.toLong())
                    //println(session.getId())
                    if(session.getUserIdFromPost() == session.getId()) {
                        findViewById<Button>(R.id.follow_btn).isVisible = false
                        findViewById<Button>(R.id.unfollow_btn).isVisible = false
                        findViewById<TextView>(R.id.edit_profile_btn).isVisible = true;
                    }
                    else {
                        sviPratioci(session.getId(), session.getUserIdFromPost())
                    }
                    if(responseBody?.image != null) {
                        var profilePhoto : Image = responseBody?.image as Image
                        Picasso.get().load(profilePhoto.url).resize(150, 150).centerCrop().into(circleIV)
                    }

                    println("Uspesan zahtev")
                }
            }

            override fun onFailure(call: Call<UserInfoItem>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })

        button.setOnClickListener {
            verifyStoragePermissions(this)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        follow_btn = findViewById(R.id.follow_btn)
        unfollow_btn = findViewById(R.id.unfollow_btn)
        edit_profile_btn = findViewById<Button>(R.id.edit_profile_btn)

        var flag = 0

        follow_btn?.setOnClickListener{
            zapratiKorisnika(session.getId(), session.getUserIdFromPost())
                var v = findViewById<TextView>(R.id.numOfFollowers).text.toString().toLong()
                v += 1
                findViewById<TextView>(R.id.numOfFollowers).text = v.toString()

            findViewById<Button>(R.id.follow_btn).isVisible = false
            findViewById<Button>(R.id.unfollow_btn).isVisible = true
        }

        unfollow_btn?.setOnClickListener{
            otpratiKorisnika(session.getId(), session.getUserIdFromPost())
            var v = findViewById<TextView>(R.id.numOfFollowers).text.toString().toLong()
            v -= 1
            findViewById<TextView>(R.id.numOfFollowers).text = v.toString()

            findViewById<Button>(R.id.follow_btn).isVisible = true
            findViewById<Button>(R.id.unfollow_btn).isVisible = false
        }
/*
        dajSvePratioce(session.getUserIdFromPost())
        dajSveKojePratim(session.getUserIdFromPost())
        dajSvePratioce(session.getUserIdFromPost())
*/

        dajStatistiku(session)

        if(findViewById<TextView>(R.id.emailuser).text.toString() == "-1")
            sviPratioci(session.getId(), session.getUserIdFromPost())

        edit_profile_btn?.setOnClickListener {
            var intent: Intent = Intent(context, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verifyStoragePermissions(activity: Activity) {
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val REQUEST_EXTERNAL_STORAGE = 1
        // check if we have write permission
        val permission: Int =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var context: Context = applicationContext;
        val session = SessionManager(context)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            var localImageUri:Uri? = data.data
            circleIV.setImageURI(data.data)
            if(localImageUri != null){
                selectedPhotoUri = localImageUri
                sendPhotoRequest(session.getUserIdFromPost())
            }
        }
    }

    fun sendPhotoRequest(userID: Long?)
    {
        var otherImagesFiles = mutableListOf<File>()
        otherImagesFiles.add(File(getRealPathFromUri(selectedPhotoUri!!)!!))

        var requestBody: RequestBody
        val mapRequestBody = LinkedHashMap<String, RequestBody>()
        val arrBody: MutableList<MultipartBody.Part> = ArrayList()

        var body: MultipartBody.Part ? = null
        Log.d(ContentValues.TAG, "file===" + otherImagesFiles[0].getName());

        println("PRVAPRVAPRVAPRVA")
        println(otherImagesFiles[0].getName())

        requestBody = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            otherImagesFiles[0]
        )
        mapRequestBody["file\"; filename=\"" + otherImagesFiles[0].getName()] = requestBody
        mapRequestBody["test"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "file")
        body = MultipartBody.Part.createFormData(
            "fileName",
            otherImagesFiles[0].getName(),
            requestBody
        )
        arrBody.add(body)

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<Image?>? = apiInterface.uploadProfileImage(mapRequestBody, userID)

        if (requestCall != null) {
            requestCall.enqueue(object : Callback<Image?> {

                override fun onResponse(
                    call: Call<Image?>,
                    response: Response<Image?>
                ) {
                    if (response.isSuccessful) {
                        println("Uspesan zahtev")
                    }
                }

                override fun onFailure(call: Call<Image?>, t: Throwable) {
                    println("Neuspesan zahtev")
                }
            })
        }
    }

    fun getRealPathFromUri(contentUri: Uri) :String? {

        var result: String? = null

        val cursor = this.contentResolver?.query(contentUri, null, null, null, null)
        if(cursor == null){
            result = contentUri.path
        }
        else{
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }

        return result
    }

    fun goToAddPost(view: View?) {
        val intent = Intent(this@ProfileOfOtherUserActivity, PostActivity::class.java)
        startActivity(intent)
    }

    private fun zapratiKorisnika(userID: Long?, userID2: Long?)
    {
        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<Follow> = apiInterface.followUser(userID, userID2)

        requestCall.enqueue(object : Callback<Follow> {

            override fun onResponse(
                call: Call<Follow>,
                response: Response<Follow>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()

                    var id = responseBody?.id.toString()
                    dajSvePratioce(userID2)
                    println("Uspesan zahtev")
                }
            }

            override fun onFailure(call: Call<Follow>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })
    }

    private fun otpratiKorisnika(userID: Long?, userID2: Long?)
    {
        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<UserInfoItem> = apiInterface.unfollowUser(userID, userID2)

        requestCall.enqueue(object : Callback<UserInfoItem> {

            override fun onResponse(
                call: Call<UserInfoItem>,
                response: Response<UserInfoItem>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()
                    dajSvePratioce(userID2)
                    println("Uspesan zahtev")
                }
            }

            override fun onFailure(call: Call<UserInfoItem>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })
    }

    private fun dajSvePratioce(userID: Long?)
    {
        var user = UserDataItem(
            userID,
            null,
            null,
            null,
            null,
            null
        );

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<Int> = apiInterface.getFollowers(user)

        requestCall.enqueue(object : Callback<Int> {

            override fun onResponse(
                call: Call<Int>,
                response: Response<Int>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()

                    findViewById<TextView>(R.id.numOfFollowers).text = responseBody.toString()

                    println("Uspesan zahtev")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })
    }

    private fun dajSveKojePratim(userID: Long?)
    {
        var user = UserDataItem(
            userID,
            null,
            null,
            null,
            null,
            null
        );

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<Int> = apiInterface.getFollowingUsers(user)

        requestCall.enqueue(object : Callback<Int> {

            override fun onResponse(
                call: Call<Int>,
                response: Response<Int>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()

                    findViewById<TextView>(R.id.numOfFollowing).text = responseBody.toString()

                    println("Uspesan zahtev")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })
    }

    private fun sviPratioci(userID: Long?, userID2: Long?)
    {
        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<List<Follow>> = apiInterface.getAllFollows()

        requestCall.enqueue(object : Callback<List<Follow>> {

            override fun onResponse(
                call: Call<List<Follow>>,
                response: Response<List<Follow>>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()!!
                    println("--------------------------------- B I T N O --------------------------------------")
                    for(r in responseBody) {
                        println(userID)
                        println("----------k")
                        if(r.from?.id == userID) {
                            var following = r.from?.following!!
                            var k = 0
                            for(r1 in following) {
                                println(r1.to?.id)
                                if(r1.to?.id == userID2) { //ukoliko pratim korisnika na cijoj sam strani
                                    k = 1
                                }
                            }
                            if(k==1){
                                findViewById<Button>(R.id.follow_btn).isVisible = false
                                findViewById<Button>(R.id.unfollow_btn).isVisible = true
                            }
                        }

                    }

                    println("Uspesan zahtev")
                }
            }

            override fun onFailure(call: Call<List<Follow>>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })

    }

    private fun dajStatistiku(session: SessionManager)
    {
        var user = UserDataItem(
            session.getUserIdFromPost(),
            null,
            null,
            null,
            null,
            null
        );

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<Statistic> = apiInterface.getStat(user)

        requestCall.enqueue(object : Callback<Statistic> {

            override fun onResponse(
                call: Call<Statistic>,
                response: Response<Statistic>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()
                    val flwrs: Long? = responseBody?.numberOfFollowers
                    val flwng: Long? = responseBody?.numberOfFollowing
                    val loc: Long? = responseBody?.numberOfLocations
                    findViewById<TextView>(R.id.numOfFollowers).text = flwrs.toString()
                    findViewById<TextView>(R.id.numOfLocations).text = loc.toString()
                    findViewById<TextView>(R.id.numOfFollowing).text = flwng.toString()
                    println("Uspesan zahtev")
                }
            }

            override fun onFailure(call: Call<Statistic>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })
    }
}