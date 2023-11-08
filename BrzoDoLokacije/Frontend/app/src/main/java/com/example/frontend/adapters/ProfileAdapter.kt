package com.example.frontend.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.frontend.ProfileInfoFragment
import com.example.frontend.ProfilePostsFragment

class ProfileAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                println("INFO");
                return ProfileInfoFragment()
            }
            1 -> {
                println("POSTS")
                return ProfilePostsFragment()
            }
            else -> {
                println("ELSE")
                return ProfileInfoFragment()
            }
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}