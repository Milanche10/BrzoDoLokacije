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
import com.example.frontend.models.Follow
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersActivity : AppCompatActivity() {

    var myFollowersD = emptyList<UserInfoItem?>().toMutableList()
    var recyclerview_followers: RecyclerView? = null
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var FollowerAdapter: FollowerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        recyclerview_followers = findViewById(R.id.recyclerView_followers)
        recyclerview_followers?.setHasFixedSize(true)

        gridLayoutManager = GridLayoutManager(applicationContext,1)
        recyclerview_followers?.layoutManager = gridLayoutManager

        val session = SessionManager(this@FollowersActivity)
        sviPratioci(session.getId())

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, ProfileActivity1::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }

    private fun sviPratioci(userID: Long?)
    {
        var followers: List<Follow>? = null
        var myFollowers = emptyList<UserInfoItem?>().toMutableList()
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
                        if(userID == r.to?.id) {
                            followers = r.to?.followers
                            for(r1 in followers!!)
                            {
                                myFollowers.add(r1.from)
                            }
                        }

                    }
                    print("FOLLOWERS:")
                    for(r1 in myFollowers.distinct()!!)
                    {
                        myFollowersD.add(r1)
                    }
                    println(myFollowersD)

                    FollowerAdapter = FollowerAdapter(baseContext, myFollowersD)
                    FollowerAdapter.notifyDataSetChanged()
                    recyclerview_followers?.adapter = FollowerAdapter

                    println("Uspesan zahtev")
                }

            }

            override fun onFailure(call: Call<List<Follow>>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })

    }
}