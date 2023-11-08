package com.example.novaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToSignUp(view: View?) {
        val intent = Intent(this@MainActivity, SignUpActivity::class.java)
        startActivity(intent)
        //Toast.makeText(this@MainActivity, "Button is Clicked", Toast.LENGTH_LONG).show()
    }

    fun goToSignIn(view: View?) {
        val intent = Intent(this@MainActivity, ProfileActivity::class.java)
        startActivity(intent)
    }

}