package com.example.frontend

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.frontend.databinding.ActivityMainBinding
import java.security.AccessController.getContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        session = SessionManager(this@MainActivity)
        if(session.isLogin() == true) {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
        }
        else {
            setContentView(binding.root)
        }


    }

    fun goToRegister(view: View?) {
        val intent = Intent(this@MainActivity, RegisterActivity1::class.java)
        startActivity(intent)
    }

    fun goToProfile(view: View?) {
        val intent = Intent(this@MainActivity, ProfileActivity1::class.java)
        startActivity(intent)
    }

    fun goToLogin(view: View?) {
        val intent = Intent(this@MainActivity, LoginActivity1::class.java)
        startActivity(intent)
    }

}