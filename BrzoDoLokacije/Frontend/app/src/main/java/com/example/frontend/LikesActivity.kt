package com.example.frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.adapters.LikesAdapter
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikesActivity : AppCompatActivity() {

    var recyclerview_likes: RecyclerView? = null
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var LikesAdapter: LikesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes)

        recyclerview_likes = findViewById(R.id.recyclerViewLikes)
        recyclerview_likes?.setHasFixedSize(true)

        gridLayoutManager = GridLayoutManager(applicationContext,1)
        recyclerview_likes?.layoutManager = gridLayoutManager

        val session = SessionManager(this)
        var post = PostInfoItem(
            null,
            null,
            session.getPostId(),
            null,
            null,
            null,
            null,
            null,
            null
        );
        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<List<LikesDataItem>> = apiInterface.getLikesForPost(post)
        requestCall.enqueue(object : Callback<List<LikesDataItem>?> {
            override fun onResponse(
                call: Call<List<LikesDataItem>?>,
                response: Response<List<LikesDataItem>?>
            ) {
                var responseBodyLike = response.body()
                LikesAdapter = LikesAdapter(baseContext, responseBodyLike!!)
                LikesAdapter.notifyDataSetChanged()
                recyclerview_likes?.adapter = LikesAdapter
            }

            override fun onFailure(call: Call<List<LikesDataItem>?>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, ProfileActivity1::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}