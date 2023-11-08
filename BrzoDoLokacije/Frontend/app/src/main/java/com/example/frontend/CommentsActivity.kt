package com.example.frontend

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.adapters.CommentAdapter
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentsActivity : AppCompatActivity() {

    var recyclerview_coments: RecyclerView? = null
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var CommentAdapter: CommentAdapter
    private lateinit var myToolbar: Toolbar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

/*        var title : TextView = findViewById(R.id.toolbar_title)
        title.text = "Komentari";
*/
        recyclerview_coments = findViewById(R.id.recyclerView_comments)
        recyclerview_coments?.setHasFixedSize(true)

        gridLayoutManager = GridLayoutManager(applicationContext,1)
        recyclerview_coments?.layoutManager = gridLayoutManager

        val session = SessionManager(this@CommentsActivity)
        getAllCommentsOnPost(session)

        findViewById<Button>(R.id.addCom).setOnClickListener{

            var appUser = UserInfoItem(
                null,
                null,
                null,
                session.getId(),
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )

            var socialPost = PostCommentInfo(
                null,
                null,
                session.getPostId(),
                null,
                null,
                null,
                null,
                null,
                null
            )

            var newComment = CommentDataItem(
                null,
                findViewById<EditText>(R.id.comment).text.toString(),
                appUser,
                socialPost
            );

            val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
            val requestCall: Call<CommentDataItem> = apiInterface.addComment(newComment)

            requestCall.enqueue(object : Callback<CommentDataItem> {

                override fun onResponse(
                    call: Call<CommentDataItem>,
                    response: Response<CommentDataItem>
                ) {
                    if (response.isSuccessful) {
                        findViewById<EditText>(R.id.comment).text.clear();
                    }
                    println("Uspesan zahtev")
                    getAllCommentsOnPost(session)
//                    val intent = Intent(applicationContext, PostInfo::class.java)
//                    startActivity(intent)

                }

                override fun onFailure(call: Call<CommentDataItem>, t: Throwable) {
                    println("Neuspesan zahtev")
                }
            })

        }
        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, PostInfo::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

//        var View = LayoutInflater.from(this).inflate(R.layout.row_comment,null)
//        System.out.println(View.findViewById<Button>(R.id.deleteCom))
//        System.out.println("USPEO SAM")
//        View.findViewById<Button>(R.id.deleteCom).setOnClickListener() {
//            val mDialogView = LayoutInflater.from(this).inflate(R.layout.delete_dialog,null)
//            val mBuilder = AlertDialog.Builder(this)
//                .setView(mDialogView)
//            val mAlertDialog = mBuilder.show()
//            mDialogView.findViewById<CircleImageView>(R.id.dialogDeleteBtn).setOnClickListener {
//                mAlertDialog.dismiss()
//            }
//        }

    }

    fun deleteComment(idComm: Long?)
    {
        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<Unit> = apiInterface.deleteComment(idComm)

        requestCall.enqueue(object : Callback<Unit> {

            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                if (response.isSuccessful) {


                }
                println("Uspesan zahtev")

//                    val intent = Intent(applicationContext, PostInfo::class.java)
//                    startActivity(intent)

            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })
    }
    private fun getAllCommentsOnPost(session: SessionManager) {

        var socialPost = PostCommentInfo(
            null,
            null,
            session.getPostId(),
            null,
            null,
            null,
            null,
            null,
            null
        )

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<List<CommentDataItem>> = apiInterface.getAllCommentsOnPost(socialPost)

        requestCall.enqueue(object : Callback<List<CommentDataItem>?> {
            override fun onResponse(
                call: Call<List<CommentDataItem>?>,
                response: Response<List<CommentDataItem>?>
            ) {
                val responseBody = response.body()!!
                CommentAdapter = CommentAdapter(baseContext, responseBody)
                CommentAdapter.notifyDataSetChanged()
                recyclerview_coments?.adapter = CommentAdapter
            }

            override fun onFailure(call: Call<List<CommentDataItem>?>, t: Throwable) {
                Log.d("HomeActivity", "onFailure:" + t.message)
            }
        })

    }
}