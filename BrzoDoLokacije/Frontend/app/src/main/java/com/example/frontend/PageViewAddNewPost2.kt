package com.example.frontend

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso

class PageViewAddNewPost2: PagerAdapter {

    var con: Context
    var path:MutableList<Image>
    lateinit var inflater: LayoutInflater

    constructor(con:Context, path: MutableList<Image>):super() {
        this.con = con
        this.path = path
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return path.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var img: ImageView
        inflater = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var rv: View = inflater.inflate(R.layout.swipe_fragment_post, container, false)
        img = rv.findViewById(R.id.post_image) as ImageView
        Picasso.get().load(path[position].url).resize(1080,1100).centerCrop().into(img)
        container.addView(rv)
        return rv
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
        `object`.visibility = View.GONE
        notifyDataSetChanged()
    }

    private fun deletePage(position: Int) {
        path.removeAt(position)
        notifyDataSetChanged()
    }
}