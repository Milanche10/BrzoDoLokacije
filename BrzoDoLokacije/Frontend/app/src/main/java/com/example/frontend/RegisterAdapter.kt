package com.example.frontend

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class RegisterAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {
    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                println("Login");
                return LoginTabFragment()
            }
            1 -> {
                println("Register")
                return RegisterTabFragment()
            }
            else -> {
                println("ELSE")
                return RegisterTabFragment()
            }
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

}