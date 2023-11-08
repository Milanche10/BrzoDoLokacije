package com.example.frontend

import android.Manifest
import android.app.Activity
import android.content.ClipData.Item
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class HomePageActivity : AppCompatActivity() {

    var gotoProfil_btn: Item? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        verifyStoragePermissions(this)

        navView.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.profile -> startActivity(
                    Intent(
                        this,
                        ProfileActivity1::class.java
                    )
                )
                /*R.id.search -> startActivity(
                    Intent(
                        this,
                        ProfileActivity1::class.java
                    )
                )
                R.id.favorite -> startActivity(
                    Intent(
                        this,
                        ProfileActivity1::class.java
                    )
                )*/
            }
            true
        }
    }

    fun goToAddPost(view: View?) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, 101)
    }

    private fun verifyStoragePermissions(activity: Activity) {
        val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val REQUEST_EXTERNAL_STORAGE = 1
        // check if we have write permission
        val permission: Int = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            val intent = Intent(this@HomePageActivity, PostActivity::class.java)
            startActivity(intent)
        }
        else {

        }
    }

}