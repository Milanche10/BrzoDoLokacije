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

class PageViewAddNewPost: PagerAdapter {

    var con: Context
    var path:MutableList<Uri>
    lateinit var inflater: LayoutInflater

    constructor(con:Context, path: MutableList<Uri>):super() {
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
        var rv: View = inflater.inflate(R.layout.swipe_fragment, container, false)
        img = rv.findViewById(R.id.post_image) as ImageView
        //img.setImageURI(path[position])
        Picasso.get().load(path[position]).resize(500,330).centerCrop().into(img)
        //var remove_button = rv.findViewById<Button>(R.id.remove_button)
        container.addView(rv)
        /*remove_button.setOnClickListener {
            destroyItem(container, position, rv)
            deletePage(position)
        }*/
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