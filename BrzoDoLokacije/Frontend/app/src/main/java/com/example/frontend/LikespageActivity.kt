package com.example.frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.activities.AddPostMapActivity
import com.example.frontend.adapters.CommentAdapter
import com.example.frontend.adapters.LikesPageAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikespageActivity : AppCompatActivity() {

    var recyclerview_like: RecyclerView? = null
    lateinit var navigationView: NavigationView
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var LikesPageAdapter: LikesPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likespage)

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        recyclerview_like = findViewById(R.id.recyclerView_likes)
        recyclerview_like?.setHasFixedSize(true)

        gridLayoutManager = GridLayoutManager(applicationContext, 3)
        recyclerview_like?.layoutManager = gridLayoutManager

        val session = SessionManager(this@LikespageActivity)

        var appUser = UserDataItem(
            session.getId(),
            null,
            null,
            null,
            null,
            null
        )

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.getMenu().findItem(R.id.favorite).setChecked(true);
        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<LikesPageDataItem> = apiInterface.getAllLikedPostsByUser(appUser)

        requestCall.enqueue(object : Callback<LikesPageDataItem> {

            override fun onResponse(
                call: Call<LikesPageDataItem>,
                response: Response<LikesPageDataItem>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    println(responseBody)
                    LikesPageAdapter = LikesPageAdapter(baseContext, responseBody)
                    LikesPageAdapter.notifyDataSetChanged()
                    recyclerview_like?.adapter = LikesPageAdapter
                    var brl: TextView = findViewById(R.id.brLajkova)
                    brl.text = "Broj lajkovanih objava " + LikesPageAdapter.itemCount.toString()
                }
                println("Uspesan zahtev")

            }

            override fun onFailure(call: Call<LikesPageDataItem>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })

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
    }

    fun goToAddPost(view: View?) {
        val intent = Intent(this@LikespageActivity, AddPostMapActivity::class.java)
        startActivity(intent)
    }
}