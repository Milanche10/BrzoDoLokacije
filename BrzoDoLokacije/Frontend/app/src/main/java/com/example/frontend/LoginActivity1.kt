package com.example.frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class LoginActivity1 : AppCompatActivity() {

    var tabLayout: TabLayout?= null
    var viewPager: ViewPager?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)

        tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        viewPager = findViewById<ViewPager>(R.id.view_pager)

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Prijava"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Registracija"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = LoginAdapter(this,  supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

}