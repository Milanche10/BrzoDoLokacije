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

class LikesPageAdapter (val context: Context, val postList: LikesPageDataItem): RecyclerView.Adapter<LikesPageAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var image: ImageView? = null
        var idP: TextView? = null

        init {
            image = itemView.findViewById(R.id.postImage)
            idP = itemView.findViewById(R.id.idPosta1)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.card_cell_profile, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val url = postList[position].socialPost.images?.get(0)?.url
        if(url!=null){
            Picasso.get().load(url).resize(150,150).centerCrop().into(holder.image)
        }
        holder.idP!!.text = postList[position].socialPost.id.toString()

        var context: Context = holder.itemView.context;
        val session = SessionManager(context)

        holder.itemView.findViewById<ImageView>(R.id.postImage).setOnClickListener {
            session.setPostId(holder.itemView.findViewById<TextView>(R.id.idPosta1).text.toString().toLong())
            val intent = Intent(context, PostInfo::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}