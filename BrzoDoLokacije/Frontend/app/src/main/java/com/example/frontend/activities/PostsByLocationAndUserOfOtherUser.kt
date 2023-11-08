package com.example.frontend.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsByLocationAndUserOfOtherUser : AppCompatActivity() {
    lateinit var PostsAdapter: PostsAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    var recyclerview_postsUser: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_by_location_and_user_of_other_user)

        var id = intent.getStringExtra("id")
        /* val session = SessionManager(this)
         var id = session.getLocationId()*/
        if (id != null) {
            println(id.toLong())
        }


        recyclerview_postsUser = findViewById(R.id.recyclerView_postsUser)
        recyclerview_postsUser?.setHasFixedSize(true)

        gridLayoutManager = GridLayoutManager(applicationContext,1)
        recyclerview_postsUser?.layoutManager = gridLayoutManager

        if (id != null) {
            getAllPostsUser(id.toLong())
        }

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, ProfileOfOtherUserActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun getAllPostsUser(id: Long) {

        var context: Context = applicationContext;
        val session = SessionManager(context)
        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        var user = UserDataItem(
            session.getUserIdFromPost(),
            null,
            null,
            null,
            null,
            null,
        );
        val requestCall: Call<List<PostDataItemLocation>> = apiInterface.getUserPosts(user)

        requestCall.enqueue(object : Callback<List<PostDataItemLocation>?> {
            override fun onResponse(
                call: Call<List<PostDataItemLocation>?>,
                response: Response<List<PostDataItemLocation>?>
            ) {
                println(id)
                println("------------- SVI POSTOVII KORISNIKAA ---------------")
                val responseBody = response.body()!!
                println(responseBody)
                var niz = emptyList<PostDataItemLocation>().toMutableList()
                for(r in responseBody)
                {
                    if(r.location!!.id == id.toInt()) {
                        niz.add(r)
                    }
                }
                PostsAdapter = PostsAdapter(baseContext, niz)
                PostsAdapter.notifyDataSetChanged()
                recyclerview_postsUser?.adapter = PostsAdapter
            }

            override fun onFailure(call: Call<List<PostDataItemLocation>?>, t: Throwable) {
                Log.d("HomeActivity", "onFailure:" + t.message)
            }
        })
    }
}