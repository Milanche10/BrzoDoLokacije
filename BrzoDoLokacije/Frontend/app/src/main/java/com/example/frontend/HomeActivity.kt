package com.example.frontend

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.frontend.activities.AddPostMapActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    lateinit var HomeAdapter: HomeAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    var recyclerview_posts: RecyclerView? = null
    private var toolBar: Toolbar? = null
    private lateinit var prefManager: SessionManager
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toggle: ActionBarDrawerToggle

    var buttonAllPosts: Button? = null
    var buttonPostsByDate: Button? = null
    var buttonPostsByRate: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        toolBar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolBar)


        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
        )
        supportActionBar?.setTitle(Html.fromHtml("<font color='#FCFCFC'></font>"));

        val session = SessionManager(this@HomeActivity)

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<UserInfoItem> = apiInterface.getUserData(session.getEmail().toString())

        requestCall.enqueue(object : Callback<UserInfoItem> {

            override fun onResponse(
                call: Call<UserInfoItem>,
                response: Response<UserInfoItem>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    session.setId(responseBody?.id)
                    System.out.println(session.getId())
                    session.setEmail(responseBody?.email)
                    session.setFirstname(responseBody?.firstName)
                    session.setLastname(responseBody?.lastName)
                    System.out.println(session.getFirstname())
                    findViewById<TextView>(R.id.imeHamMenu).text = session.getFirstname() + " " + session.getLastname()
                    findViewById<TextView>(R.id.mailHamMenu).text = session.getEmail()

                    var slika : CircleImageView = findViewById<CircleImageView>(R.id.photoHamMenu)
                    if(responseBody?.image != null) {
                        var profilePhoto : Image = responseBody?.image as Image
                        Picasso.get().load(profilePhoto.url).resize(150, 150).centerCrop().into(slika)
                    }
                }
                println("Uspesan zahtev")
            }

            override fun onFailure(call: Call<UserInfoItem>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })

        //verifyStoragePermissions(this)

        navView.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.profile -> startActivity(
                    Intent(
                        this,
                        ProfileActivity1::class.java
                    )
                )
                R.id.search -> startActivity(
                    Intent(
                        this,
                        MapActivity::class.java
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

        prefManager = SessionManager(applicationContext)
        recyclerview_posts = findViewById(R.id.recyclerView_posts)
        recyclerview_posts?.setHasFixedSize(true)


        gridLayoutManager = GridLayoutManager(applicationContext,1)
        recyclerview_posts?.layoutManager = gridLayoutManager

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.profileham -> startActivity(
                    Intent(
                        this,
                        EditProfileActivity::class.java
                    )
                )
                R.id.passwordChange -> startActivity(
                    Intent(
                        this,
                        ChangePasswordActivity::class.java
                    )
                )
                R.id.logout -> {
                    prefManager.removeData()
                    var intent: Intent = Intent(this, LoginActivity1::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        getAllPosts()

        buttonAllPosts = findViewById(R.id.button4)
        buttonAllPosts?.setOnClickListener {
            getAllPosts()
        }

        buttonPostsByDate = findViewById(R.id.button5)
        buttonPostsByDate?.setOnClickListener {
            getAllPostsSortedByDate()
        }

        buttonPostsByRate = findViewById(R.id.button6)
        buttonPostsByRate?.setOnClickListener {
            getAllPostsSortedByRate()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getAllPosts() {

        val apiInterface: ApiInterface =
            ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<List<PostDataItem>> = apiInterface.getAllPost()

        requestCall.enqueue(object : Callback<List<PostDataItem>?> {
            override fun onResponse(
                call: Call<List<PostDataItem>?>,
                response: Response<List<PostDataItem>?>
            ) {
                val responseBody = response.body()!!
                println(responseBody)
                HomeAdapter = HomeAdapter(baseContext, responseBody)
                HomeAdapter.notifyDataSetChanged()
                recyclerview_posts?.adapter = HomeAdapter
            }

            override fun onFailure(call: Call<List<PostDataItem>?>, t: Throwable) {
                d("HomeActivity","onFailure:" + t.message)
            }
        })

    }

    private fun getAllPostsSortedByDate() {

        val apiInterface: ApiInterface =
            ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<List<PostDataItem>> = apiInterface.getAllPostSortedByDate()

        requestCall.enqueue(object : Callback<List<PostDataItem>?> {
            override fun onResponse(
                call: Call<List<PostDataItem>?>,
                response: Response<List<PostDataItem>?>
            ) {
                val responseBody = response.body()!!
                println(responseBody)
                HomeAdapter = HomeAdapter(baseContext, responseBody)
                HomeAdapter.notifyDataSetChanged()
                recyclerview_posts?.adapter = HomeAdapter
            }

            override fun onFailure(call: Call<List<PostDataItem>?>, t: Throwable) {
                d("HomeActivity","onFailure:" + t.message)
            }
        })
    }

    private fun getAllPostsSortedByRate() {

        val apiInterface: ApiInterface =
            ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<List<PostDataItem>> = apiInterface.getAllPostSortedByRate()

        requestCall.enqueue(object : Callback<List<PostDataItem>?> {
            override fun onResponse(
                call: Call<List<PostDataItem>?>,
                response: Response<List<PostDataItem>?>
            ) {
                println(response)
                val responseBody = response.body()!!
                HomeAdapter = HomeAdapter(baseContext, responseBody)
                HomeAdapter.notifyDataSetChanged()
                recyclerview_posts?.adapter = HomeAdapter
            }

            override fun onFailure(call: Call<List<PostDataItem>?>, t: Throwable) {
                d("HomeActivity","onFailure:" + t.message)
            }
        })
    }

    fun goToAddPost(view: View?) {
        /*
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, 101)
        */
         */

        val intent = Intent(this@HomeActivity, AddPostMapActivity::class.java)
        startActivity(intent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            val intent = Intent(this@HomeActivity, PostActivity::class.java)
            startActivity(intent)
        }
        else {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.header_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }
}