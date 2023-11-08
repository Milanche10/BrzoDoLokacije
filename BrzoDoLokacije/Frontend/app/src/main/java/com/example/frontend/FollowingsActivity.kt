package com.example.frontend

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.adapters.CommentAdapter
import com.example.frontend.adapters.FollowerAdapter
import com.example.frontend.adapters.FollowingAdapter
import com.example.frontend.models.Follow
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingsActivity : AppCompatActivity() {

    var myFollowingsD = emptyList<UserInfoItem?>().toMutableList()
    var recyclerview_followings: RecyclerView? = null
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var FollowingAdapter: FollowingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followings)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        recyclerview_followings = findViewById(R.id.recyclerView_followings)
        recyclerview_followings?.setHasFixedSize(true)

        gridLayoutManager = GridLayoutManager(applicationContext,1)
        recyclerview_followings?.layoutManager = gridLayoutManager

        val session = SessionManager(this@FollowingsActivity)
        svaPracenja(session.getId())

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, ProfileActivity1::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }

    private fun svaPracenja(userID: Long?)
    {
        var followings: List<Follow>? = null
        var myFollowings = emptyList<UserInfoItem?>().toMutableList()
        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<List<Follow>> = apiInterface.getAllFollows()

        requestCall.enqueue(object : Callback<List<Follow>> {

            override fun onResponse(
                call: Call<List<Follow>>,
                response: Response<List<Follow>>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()!!
                    //println(responseBody.toString())

                    for(r in responseBody) {
                        if(userID == r.from?.id) {
                            followings = r.from?.following
                            for(r1 in followings!!)
                            {
                                myFollowings.add(r1.to)
                            }
                        }

                    }
                    print("FOLLOWERS:")
                    for(r1 in myFollowings.distinct()!!)
                    {
                        myFollowingsD.add(r1)
                    }
                    println(myFollowingsD)

                    FollowingAdapter = FollowingAdapter(baseContext, myFollowingsD)
                    FollowingAdapter.notifyDataSetChanged()
                    recyclerview_followings?.adapter = FollowingAdapter

                    println("Uspesan zahtev")
                }

            }

            override fun onFailure(call: Call<List<Follow>>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })

    }
}