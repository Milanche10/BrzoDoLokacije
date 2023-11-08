package com.example.frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsByLocation : AppCompatActivity() {

    lateinit var PostsAdapter: PostsAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    var recyclerview_posts: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_by_location)

        var id = intent.getStringExtra("id")
       /* val session = SessionManager(this)
        var id = session.getLocationId()*/
        if (id != null) {
            println(id.toInt())
        }

        recyclerview_posts = findViewById(R.id.recyclerView_posts)
        recyclerview_posts?.setHasFixedSize(true)

        gridLayoutManager = GridLayoutManager(applicationContext,1)
        recyclerview_posts?.layoutManager = gridLayoutManager

        if (id != null) {
            getAllPosts(id.toInt())
        }

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun getAllPosts(id: Int) {

        val apiInterface: ApiInterface =
            ServiceBuilder.buildService(ApiInterface::class.java)
        var post = PostDataItemLocation(
            null,
            null,
            id,
            null,
            null,
            null,
        );
        val requestCall: Call<List<PostDataItemLocation>> = apiInterface.getAllPostsByLocation(post)

        requestCall.enqueue(object : Callback<List<PostDataItemLocation>?> {
            override fun onResponse(
                call: Call<List<PostDataItemLocation>?>,
                response: Response<List<PostDataItemLocation>?>
            ) {
                val responseBody = response.body()!!
                println(responseBody)
                PostsAdapter = PostsAdapter(baseContext, responseBody)
                PostsAdapter.notifyDataSetChanged()
                recyclerview_posts?.adapter = PostsAdapter
            }

            override fun onFailure(call: Call<List<PostDataItemLocation>?>, t: Throwable) {
                Log.d("HomeActivity", "onFailure:" + t.message)
            }
        })
    }
}