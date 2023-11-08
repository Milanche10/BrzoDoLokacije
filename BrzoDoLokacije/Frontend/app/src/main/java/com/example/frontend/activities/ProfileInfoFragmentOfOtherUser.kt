package com.example.frontend.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileInfoFragmentOfOtherUser : Fragment() {
    private lateinit var prefManager: SessionManager
    lateinit var ProfilePostAdapter: ProfilePostAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater,
                          container: ViewGroup?,
                          savedInstanceState: Bundle?):View?{
        super.onCreate(savedInstanceState)
        var view : View = inflater.inflate(R.layout.activity_profile_info_fragment_of_other_user, container, false) as ViewGroup
        var context: Context = view.getContext();
        prefManager = SessionManager(context)

        if(prefManager.isLogin() == false) {
            var intent: Intent = Intent(context, LoginActivity1::class.java)
            startActivity(intent)
        }

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
                    session.setUserIdFromPost(responseBody?.id)

                    getUserPosts(context,session)
                }
                println("Uspesan zahtev")
            }

            override fun onFailure(call: Call<UserInfoItem>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.setHasFixedSize(true)


        gridLayoutManager = GridLayoutManager(view.context,3)
        recyclerView?.layoutManager = gridLayoutManager

        return view;
    }

    private fun getUserPosts(context: Context,session: SessionManager) {

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)

        var newUser = UserDataItem(session.getUserIdFromPost(),null,null,null,null);
        val requestCall: Call<List<PostDataItemLocation>> = apiInterface.getUserPosts(newUser)
        requestCall.enqueue(object : Callback<List<PostDataItemLocation>?> {
            override fun onResponse(
                call: Call<List<PostDataItemLocation>?>,
                response: Response<List<PostDataItemLocation>?>
            ) {
                val responseBody = response.body()!!

                ProfilePostAdapter = ProfilePostAdapter(context, responseBody)
                ProfilePostAdapter.notifyDataSetChanged()
                recyclerView?.adapter = ProfilePostAdapter
                System.out.println(responseBody)
            }

            override fun onFailure(call: Call<List<PostDataItemLocation>?>, t: Throwable) {
                Log.d("ProfileActivity1", "onFailure:" + t.message)
            }
        })
    }

    fun odjaviSe(view: View){
        prefManager.removeData()
        var context: Context = view.getContext();
        var intent: Intent = Intent(context, LoginActivity1::class.java)
        startActivity(intent)
    }
}