package com.example.frontend.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.frontend.activities.ProfileInfoFragmentOfOtherUser
import com.example.frontend.activities.ProfileOfOtherUserPostsFragment

class ProfileOfOtherUserAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                println("INFO");
                return ProfileInfoFragmentOfOtherUser()
            }
            1 -> {
                println("POSTS")
                return ProfileOfOtherUserPostsFragment()
            }
            else -> {
                println("ELSE")
                return ProfileInfoFragmentOfOtherUser()
            }
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}