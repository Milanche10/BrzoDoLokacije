package com.example.novaapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyAdapter(val context: Context, val userList: List<MyDataItem>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        var title:TextView? = null
        var button:Button? = null
        var ime:TextView? = null
        var prezime:TextView? = null

        init{

            title = itemView.findViewById(R.id.title)
            button = itemView.findViewById(R.id.button)
            ime = itemView.findViewById(R.id.ime)
            prezime = itemView.findViewById(R.id.prezime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var itemView = LayoutInflater.from(context).inflate(R.layout.row_items, parent, false)
        return ViewHolder((itemView))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title!!.text = userList[position].email
        holder.ime!!.text = userList[position].first_name
        holder.prezime!!.text = userList[position].last_name
        holder.button!!.setOnClickListener { obrisi(userList[position]) }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun obrisi(myDataItem: MyDataItem)
    {
        println(myDataItem.email);

        val url = "http://192.168.0.17:9080/api/v1/"
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()
            .create(ApiInterface::class.java)

        var retrofitData = retrofitBuilder.deleteData(myDataItem.email)

        retrofitData.enqueue(object : Callback<Unit?> {
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                println("Uspelo")
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                println("Nije uspelo")
            }
        })
    }
}

