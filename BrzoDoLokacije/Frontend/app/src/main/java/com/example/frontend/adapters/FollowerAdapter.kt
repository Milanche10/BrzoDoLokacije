package com.example.frontend.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class FollowerAdapter (val context: Context, val postList: List<UserInfoItem?>): RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var image: CircleImageView? = null
        var name: TextView? = null

        init {
            image = itemView.findViewById(R.id.follower_img)
            name = itemView.findViewById(R.id.follower_username)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_follower, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val url = postList[position]?.image?.url
        Picasso.get().load(url).into(holder.image)

        holder.name!!.text = postList[position]?.firstName + " " + postList[position]?.lastName

    }

    override fun getItemCount(): Int {
        return postList.size
    }
}