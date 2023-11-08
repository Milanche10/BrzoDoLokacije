package com.example.frontend

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PostsAdapter (val context: Context, val postList: List<PostDataItemLocation>): RecyclerView.Adapter<PostsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView? = null
        var location: TextView? = null
        var idP: TextView? = null

        //var imageBitMap: Bitmap? = null
        // val handler = Handler(Looper.getMainLooper())
        init {
            image = itemView.findViewById(R.id.imagePost)
            location = itemView.findViewById(R.id.locationPost)
            idP = itemView.findViewById(R.id.idPosta)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.card_cell, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val url = postList[position].images!![0].url
        if(url != null) {
            Picasso.get().load(url).resize(500, 330).centerCrop().into(holder.image)
        }
        holder.location!!.text = postList[position].location!!.name
        holder.idP!!.text = postList[position].id.toString()

        var context: Context = holder.itemView.context;
        val session = SessionManager(context)

        holder.itemView.findViewById<ImageView>(R.id.imagePost).setOnClickListener {
            session.setPostId(holder.itemView.findViewById<TextView>(R.id.idPosta).text.toString().toLong())
            val intent = Intent(context, PostInfo::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}