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

class ProfilePostAdapter(val context: Context, val postList: List<PostDataItemLocation>): RecyclerView.Adapter<ProfilePostAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var image: ImageView? = null
        var idP1: TextView? = null

        init {
            image = itemView.findViewById(R.id.postImage)
            idP1 = itemView.findViewById(R.id.idPosta1)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.card_cell_profile, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = postList[position].images?.get(0)?.url
        holder.idP1!!.text = postList[position].id.toString()
        Picasso.get().load(url).resize(150, 150).centerCrop().into(holder.image)
        var context: Context = holder.itemView.context;
        val session = SessionManager(context)

        holder.itemView.findViewById<ImageView>(R.id.postImage).setOnClickListener {
            session.setPostId(
                holder.itemView.findViewById<TextView>(R.id.idPosta1).text.toString().toLong()
            )
            val intent = Intent(context, PostInfo::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}