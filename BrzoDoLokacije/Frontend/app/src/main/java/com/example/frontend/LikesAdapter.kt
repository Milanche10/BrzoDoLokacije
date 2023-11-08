package com.example.frontend.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class LikesAdapter (val context: Context, val postList: List<LikesDataItem>): RecyclerView.Adapter<LikesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var image: CircleImageView? = null
        var name: TextView? = null
        init {
            image = itemView.findViewById(R.id.imageUser)
            name = itemView.findViewById(R.id.username)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.card_cell_likes, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val url = postList[position].appUser.image?.url
        if(url != null) {
            Picasso.get().load(url).into(holder.image)
        }
        holder.name!!.text = postList[position].appUser.firstName + " " +  postList[position].appUser.lastName
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}
