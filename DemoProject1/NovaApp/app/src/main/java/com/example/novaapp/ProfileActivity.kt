package com.example.novaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class ProfileActivity : AppCompatActivity() {
    var tv_first_name: TextView? = null
    var tv_last_name: TextView? = null
    var tv_email: TextView? = null
    var txtId: TextView? = null
    var sign_out_btn: Button? = null
    var recyclerview_users: RecyclerView? = null
    var button: Button? = null

    lateinit var myAdapter: MyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        recyclerview_users = findViewById(R.id.recyclerview_users)
        recyclerview_users?.setHasFixedSize(true)


        linearLayoutManager = LinearLayoutManager(this)
        recyclerview_users?.layoutManager = linearLayoutManager

        ucitajSveKorisnike()
    }

    fun ucitajSveKorisnike() {

        val url = "http://192.168.0.17:9080/api/v1/"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()
            .create(ApiInterface::class.java)

        var retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<List<MyDataItem>?> {
            override fun onResponse(
                call: Call<List<MyDataItem>?>,
                response: Response<List<MyDataItem>?>
            ) {
                val responseBody = response.body()!!


                myAdapter = MyAdapter(baseContext, responseBody)
                myAdapter.notifyDataSetChanged()
                recyclerview_users?.adapter = myAdapter


            }

            override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {
                d("ProfileActivity", "onFailure: " + t.message)
            }
        })

    }


    fun signUserOut(view: View?) {

        //Return User Back To Home
        val goToHome = Intent(this@ProfileActivity, MainActivity::class.java)
        startActivity(goToHome)
        finish()
    }

    fun refreshujStranu(view: View?)
    {
        ucitajSveKorisnike()
    }
}