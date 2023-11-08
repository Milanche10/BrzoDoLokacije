package com.example.frontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class RegisterActivity1 : AppCompatActivity() {

    var tabLayout: TabLayout?= null
    var viewPager: ViewPager?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)

        tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        viewPager = findViewById<ViewPager>(R.id.view_pager)

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Prijava"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Registracija"),1,true)
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = RegisterAdapter(this,  supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        println(viewPager!!.currentItem)
        viewPager!!.currentItem = 1
        println(viewPager!!.currentItem)
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                println(tabLayout!!.selectedTabPosition)
                if(tabLayout!!.selectedTabPosition == 1 && viewPager!!.currentItem == 0)
                    viewPager!!.currentItem = 1
                else
                    if(tabLayout!!.selectedTabPosition == 1 && viewPager!!.currentItem == 1)
                        viewPager!!.currentItem = 1
                else
                    if(tabLayout!!.selectedTabPosition == 0 && viewPager!!.currentItem == 0)
                        viewPager!!.currentItem = 0
                    else
                        if(tabLayout!!.selectedTabPosition == 0 && viewPager!!.currentItem == 1)
                            viewPager!!.currentItem = 0
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }
}