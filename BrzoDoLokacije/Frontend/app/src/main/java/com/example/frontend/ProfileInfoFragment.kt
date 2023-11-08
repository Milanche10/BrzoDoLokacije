package com.example.frontend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileInfoFragment : Fragment() {

    private lateinit var prefManager: SessionManager
    private lateinit var token: String
    lateinit var ProfilePostAdapter: ProfilePostAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    var recyclerView: RecyclerView? = null

    var sign_out_btn: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        var view : View = inflater.inflate(R.layout.profile_info_fragment, container, false) as ViewGroup

        var context: Context = view.getContext();

        prefManager = SessionManager(context)
        token = prefManager.getToken().toString()

        if(prefManager.isLogin() == false) {
            var intent: Intent = Intent(context, LoginActivity1::class.java)
            startActivity(intent)
        }

//        sign_out_btn = view?.findViewById(R.id.button4)
//        sign_out_btn?.setOnClickListener {
//            prefManager.removeData()
//            var context: Context = view.getContext();
//            var intent: Intent = Intent(context, LoginActivity1::class.java)
//            startActivity(intent)
//
//        }

        val session = SessionManager(context)
        System.out.println(session.getEmail().toString())

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
                    getUserPosts(context,session)
                    //view.findViewById<TextView>(R.id.firstname).text = responseBody?.firstName
                    //view.findViewById<TextView>(R.id.lastname).text = responseBody?.lastName
                    //view.findViewById<TextView>(R.id.email).text = responseBody?.email

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
        System.out.println(session.getId());
        var newUser = UserDataItem(session.getId(),null,null,null,null);
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