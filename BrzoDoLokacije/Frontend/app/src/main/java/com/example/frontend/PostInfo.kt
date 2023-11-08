package com.example.frontend

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.example.frontend.activities.AddPostMapActivity
import com.example.frontend.activities.ProfileOfOtherUserActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.util.*


class PostInfo : AppCompatActivity() {

    private lateinit var postPhotos: ImageView;
    private lateinit var userPhoto: CircleImageView;

    var postPhoto = emptyList<Image>().toMutableList()

    lateinit var dotsLayout: LinearLayout
    lateinit var mPager: ViewPager
    lateinit var dots: Array<ImageView>
    lateinit var adapter: PageViewAddNewPost2

    var currentPage: Int = 0
    lateinit var timer: Timer
    val DELAY_MS: Long = 5000
    val PERIOD_MS: Long = 5000

    var opis: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_info)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        navView.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.home -> startActivity(
                    Intent(
                        this,
                        HomeActivity::class.java
                    )
                )
                R.id.profile -> startActivity(
                    Intent(
                        this,
                        ProfileActivity1::class.java
                    )
                )
                R.id.search -> startActivity(
                    Intent(
                        this,
                        MapActivity::class.java
                    )
                )
                R.id.favorite -> startActivity(
                    Intent(
                        this,
                        LikespageActivity::class.java
                    )
                )
            }
            true
        }

        mPager = findViewById<View>(R.id.pager) as ViewPager
        adapter = PageViewAddNewPost2(this, postPhoto)

        mPager.adapter = adapter
        dotsLayout = findViewById(R.id.dotsLayout)

        //createDots(0)
        //updatePage()

        if (postPhoto.isEmpty()) {
            mPager.visibility = View.GONE
        }

        opis = findViewById<TextView>(R.id.opisPosta)
        opis!!.movementMethod = ScrollingMovementMethod()

        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //end
            }

            override fun onPageSelected(position: Int) {
                createDots(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                //end
            }
        })

        var context: Context = applicationContext;
        val session = SessionManager(context)
        var likeReactionId: Long? = null

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        val requestCall: Call<PostInfoItem> = apiInterface.getPostInfo(session.getPostId())

        requestCall.enqueue(object : Callback<PostInfoItem> {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<PostInfoItem>,
                response: Response<PostInfoItem>
            ) {
                if (response.isSuccessful) {
                      System.out.println(response.body())
                      var responseBody = response.body()
                      findViewById<TextView>(R.id.opisPosta).text = responseBody?.description
                      findViewById<TextView>(R.id.lokacPosta).text = responseBody?.location?.name
                      userPhoto = findViewById(R.id.slikaUser)
                      findViewById<TextView>(R.id.imeUser).text = responseBody?.appUser?.lastName + " " + responseBody?.appUser?.firstName
                      Picasso.get().load(responseBody?.appUser?.image?.url).into(userPhoto)
                      findViewById<TextView>(R.id.datumPosta).text = "Decembar 2022"
                      findViewById<TextView>(R.id.brojLajkova).text = responseBody?.likeReactions?.size.toString()
                      println(responseBody?.likeReactions?.size.toString())

                    findViewById<TextView>(R.id.idUser).text = responseBody?.appUser?.id.toString()
                    session.setUserIdFromPost(responseBody?.appUser?.id)
                    session.setUserEmailFromPost(responseBody?.appUser?.email)

                    var post = PostInfoItem(
                        null,
                        null,
                        session.getPostId(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    );
                    val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
                    val requestCall: Call<List<LikesDataItem>> = apiInterface.getLikesForPost(post)
                    requestCall.enqueue(object : Callback<List<LikesDataItem>?> {
                        override fun onResponse(
                            call: Call<List<LikesDataItem>?>,
                            response: Response<List<LikesDataItem>?>
                        ) {
                            var responseBodyLike = response.body()
                            var ind = 0;
                            if (responseBodyLike != null) {
                                for (like in responseBodyLike)
                                {
                                    if(like.appUser.id == session.getId())
                                    {
                                        ind = 1;
                                        findViewById<CircleImageView>(R.id.praznoSrce).isVisible=false
                                        findViewById<CircleImageView>(R.id.punoSrce).isVisible=true
                                        likeReactionId =  like.id!!
                                    }
                                }
                            }
                            if(ind == 0) {
                                findViewById<CircleImageView>(R.id.praznoSrce).isVisible=true
                                findViewById<CircleImageView>(R.id.punoSrce).isVisible=false
                            }
                        }

                        override fun onFailure(call: Call<List<LikesDataItem>?>, t: Throwable) {
                            println("Neuspesan zahtev")
                        }
                    })
                    /* var ind = 0;
                      var niz: List<LikeDataItem> = responseBody!!.likeReactions
                      for (i in niz.indices) {
                            if(niz[i].appUser.id == session.getId())
                            {
                                ind = 1;
                                findViewById<CircleImageView>(R.id.praznoSrce).isVisible=false
                                findViewById<CircleImageView>(R.id.punoSrce).isVisible=true
                                likeReactionId =  niz[i].id!!
                            }
                        }
                        if(ind == 0) {
                            findViewById<CircleImageView>(R.id.praznoSrce).isVisible=true
                            findViewById<CircleImageView>(R.id.punoSrce).isVisible=false
                        }*/


                      postPhotos = findViewById(R.id.postSlike)

                      if(responseBody?.images != null) {

                          for (i in 0 until responseBody?.images!!.size)
                          {
                              postPhoto.add(responseBody?.images!![i])
                              //Picasso.get().load(postPhoto.url).resize(1080,1100).centerCrop().into(postPhotos)
                          }

                          adapter = PageViewAddNewPost2(this@PostInfo, postPhoto)
                          mPager.adapter = adapter
                          mPager.visibility = View.VISIBLE
                          createDots(0)
                          updatePage()
                      }

                    println("Uspesan zahtev")
                }
            }

            override fun onFailure(call: Call<PostInfoItem>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })

        findViewById<TextView>(R.id.vidiKomentare).setOnClickListener {
            val intent = Intent(this@PostInfo, CommentsActivity::class.java)
            startActivity(intent)
        }
        //findViewById<CircleImageView>(R.id.praznoSrce).isVisible=false
        //findViewById<CircleImageView>(R.id.punoSrce).isVisible=true
        findViewById<CircleImageView>(R.id.dodajUOmiljeno).setOnClickListener{

            if(!findViewById<CircleImageView>(R.id.punoSrce).isVisible) {

                var newLike = LikesDataItem(
                    UserInfoItem(null, null, null, session.getId(), null, null, null, null, null),
                    null,
                    SocialPost(null, null, null, null, session.getPostId(), null, null, null, null)
                );
                var post = PostInfoItem(
                    null,
                    null,
                    session.getPostId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                );
                val apiInterface: ApiInterface =
                    ServiceBuilder.buildService(ApiInterface::class.java)
                val requestCall: Call<LikesDataItem> = apiInterface.addLike(newLike)
                requestCall.enqueue(object : Callback<LikesDataItem?> {
                    override fun onResponse(
                        call: Call<LikesDataItem?>,
                        response: Response<LikesDataItem?>
                    ) {
                        findViewById<CircleImageView>(R.id.praznoSrce).isVisible=false
                        findViewById<CircleImageView>(R.id.punoSrce).isVisible=true


                        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
                        val requestCall: Call<List<LikesDataItem>> = apiInterface.getLikesForPost(post)
                        requestCall.enqueue(object : Callback<List<LikesDataItem>?> {
                            override fun onResponse(
                                call: Call<List<LikesDataItem>?>,
                                response: Response<List<LikesDataItem>?>
                            ) {
                                var responseLike = response.body()
                                findViewById<TextView>(R.id.brojLajkova).text = responseLike!!.size.toString()
                                var ind = 0;
                                if (responseLike != null) {
                                    for (like in responseLike)
                                    {
                                        if(like.appUser.id == session.getId())
                                        {
                                            ind = 1;
                                         //   findViewById<CircleImageView>(R.id.praznoSrce).isVisible=false
                                         //   findViewById<CircleImageView>(R.id.punoSrce).isVisible=true
                                            likeReactionId =  like.id!!
                                        }
                                    }
                                }
                                if(ind == 0) {
                                    //findViewById<CircleImageView>(R.id.praznoSrce).isVisible=true
                                    //findViewById<CircleImageView>(R.id.punoSrce).isVisible=false
                                }
                            }

                            override fun onFailure(call: Call<List<LikesDataItem>?>, t: Throwable) {
                                println("Neuspesan zahtev")
                            }
                        })
                    }

                    override fun onFailure(call: Call<LikesDataItem?>, t: Throwable) {
                        println("Neuspesan zahtev")
                    }
                })
            }
            else
            {
                val apiInterface: ApiInterface =
                    ServiceBuilder.buildService(ApiInterface::class.java)
                println("######## LIKE REACTION ID #######")
                println(likeReactionId)
                val requestCall: Call<Unit> = apiInterface.deleteLike(likeReactionId!!)

                var post = PostInfoItem(
                    null,
                    null,
                    session.getPostId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                );

                requestCall.enqueue(object : Callback<Unit?> {
                    override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                        findViewById<CircleImageView>(R.id.praznoSrce).isVisible=true
                        findViewById<CircleImageView>(R.id.punoSrce).isVisible=false

                        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
                        val requestCall: Call<List<LikesDataItem>> = apiInterface.getLikesForPost(post)
                        requestCall.enqueue(object : Callback<List<LikesDataItem>?> {
                            override fun onResponse(
                                call: Call<List<LikesDataItem>?>,
                                response: Response<List<LikesDataItem>?>
                            ) {
                                var responseLike = response.body()
                                findViewById<TextView>(R.id.brojLajkova).text = responseLike!!.size.toString()
                            }

                            override fun onFailure(call: Call<List<LikesDataItem>?>, t: Throwable) {
                                println("Neuspesan zahtev")
                            }
                        })
                    }

                    override fun onFailure(call: Call<Unit?>, t: Throwable) {
                        println("Neuspesan zahtev");
                    }
                })

            }
        }

        findViewById<CircleImageView>(R.id.komentaricon).setOnClickListener {
            val intent = Intent(this@PostInfo, CommentsActivity::class.java)
            startActivity(intent)
        }

        izbrojKomentare(session)

        findViewById<CircleImageView>(R.id.slikaUser).setOnClickListener {
            if(session.getId() == session.getUserIdFromPost()){
                val intent = Intent(this@PostInfo, ProfileActivity1::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this@PostInfo, ProfileOfOtherUserActivity::class.java)
                startActivity(intent)
            }
        }

        findViewById<TextView>(R.id.imeUser).setOnClickListener {
            if(session.getId() == session.getUserIdFromPost()){
                val intent = Intent(this@PostInfo, ProfileActivity1::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this@PostInfo, ProfileOfOtherUserActivity::class.java)
                startActivity(intent)
            }
        }

    }

    fun izbrojKomentare(session: SessionManager){
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
        val requestCall1: Call<Long> = apiInterface.getCommentNumber(socialPost)

        requestCall1.enqueue(object : Callback<Long> {

            override fun onResponse(
                call: Call<Long>,
                response: Response<Long>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    findViewById<TextView>(R.id.brojKom).text = responseBody.toString()
                    var brKom: Int = findViewById<TextView>(R.id.brojKom).text.toString().toInt()
                    if(brKom == 0)
                        findViewById<TextView>(R.id.vidiKomentare).isVisible = false
                    println("Uspesan zahtev")
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                println("Neuspesan zahtev")
            }
        })
        //startActivity(intent)
        findViewById<TextView>(R.id.brojLajkova).setOnClickListener {

            val intent = Intent(this@PostInfo, LikesActivity::class.java)
            startActivity(intent)
        }
    }

    fun formirajDatum(datum: String): CharSequence? {
        var d: String = datum.take(10);
        if(d.equals("null"))
            return "";
        else
        {
            if(d.substring(5,7).equals("01"))
                d = d.takeLast(2) + " januar " + d.take(4)
            else if(d.substring(5,7).equals("02"))
                d = d.takeLast(2) + " februar " + d.take(4)
            else if(d.substring(5,7).equals("03"))
                d = d.takeLast(2) + " mart " + d.take(4)
            else if(d.substring(5,7).equals("04"))
                d = d.takeLast(2) + " april " + d.take(4)
            else if(d.substring(5,7).equals("05"))
                d = d.takeLast(2) + " maj " + d.take(4)
            else if(d.substring(5,7).equals("06"))
                d = d.takeLast(2) + " jun " + d.take(4)
            else if(d.substring(5,7).equals("07"))
                d = d.takeLast(2) + " jul " + d.take(4)
            else if(d.substring(5,7).equals("08"))
                d = d.takeLast(2) + " avgust " + d.take(4)
            else if(d.substring(5,7).equals("09"))
                d = d.takeLast(2) + " septembar " + d.take(4)
            else if(d.substring(5,7).equals("10"))
                d = d.takeLast(2) + " oktobar " + d.take(4)
            else if(d.substring(5,7).equals("11"))
                d = d.takeLast(2) + " novembar " + d.take(4)
            else
                d = d.takeLast(2) + " decembar " + d.take(4)
        }
        return d;
    }

    fun goToAddPost(view: View?) {
        val intent = Intent(this@PostInfo, AddPostMapActivity::class.java)
        startActivity(intent)
    }

    private fun updatePage() {
        var handler = Handler()
        val update = Runnable {
            if (currentPage == postPhoto.size) {
                currentPage = 0
            }
            mPager.setCurrentItem(currentPage++, true)
        }
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    fun createDots(position: Int) {
        if (dotsLayout != null) {
            dotsLayout.removeAllViews()
        }
        dots = Array(postPhoto.size) { ImageView(this) }

        if(postPhoto.size>1) {
            for (i in 0 until postPhoto.size) {
                dots[i] = ImageView(this)
                if (i == position) {
                    dots[i].setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.active_dots
                        )
                    )
                } else {
                    dots[i].setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.inactive_dots
                        )
                    )
                }

                var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(4, 0, 4, 0)
                dotsLayout.addView(dots[i], params)
            }
        }
    }

}