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

class CommentAdapter (val context: Context, val postList: List<CommentDataItem>): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var image: CircleImageView? = null
        var name: TextView? = null
        var content: TextView? = null
        var email: TextView? = null
        var commId: TextView? = null
        init {
            commId = itemView.findViewById(R.id.comment_id)
            email = itemView.findViewById(R.id.idUser)
            image = itemView.findViewById(R.id.comment_img)
            name = itemView.findViewById(R.id.comment_username)
            content = itemView.findViewById(R.id.comment_content)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_comment, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val url = postList[position].appUser?.image?.url
        if(url != null) {
            Picasso.get().load(url).into(holder.image)
        }
        holder.email!!.text = postList[position].appUser?.email.toString()
        holder.name!!.text = postList[position].appUser?.firstName + " " + postList[position].appUser?.lastName
        holder.content!!.text = postList[position].content.toString()
        holder.commId!!.text = postList[position].id.toString()

//        var context: Context = holder.itemView.context;
//        val session = SessionManager(context)
////
        holder.itemView.findViewById<CircleImageView>(R.id.comment_delete).setOnClickListener {
              CommentsActivity().deleteComment(postList[position].id?.toLong())
              val intent = Intent(context, CommentsActivity::class.java)
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              context.startActivity(intent)
//            val mDialogView = LayoutInflater.from(context).inflate(R.layout.delete_dialog,null)
//            val mBuilder = AlertDialog.Builder(context)
//                .setView(mDialogView)
//            val mAlertDialog = mBuilder.show()
//            mDialogView.findViewById<Button>(R.id.dialogDeleteBtn).setOnClickListener {
//                mAlertDialog.dismiss()
//            }

        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}