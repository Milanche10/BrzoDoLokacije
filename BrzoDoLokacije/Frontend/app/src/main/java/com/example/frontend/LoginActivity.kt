package com.example.frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.frontend.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    var email: EditText? = null
    var password: EditText? = null
    var sign_in_btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = binding.emailEditText
        password = binding.passwordEditText
        sign_in_btn = findViewById(R.id.sign_in_btn)

        emailFocusListener()
        passwordFocusListener()

        sign_in_btn?.setOnClickListener {
            if (submitForm() == 1) {
                var newUser = UserDataItem(
                    null,
                    null,
                    email!!.text.toString(),
                    password!!.text.toString()
                );

                val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
                val requestCall: Call<UserDataItem> = apiInterface.loginUser(newUser)

                requestCall.enqueue(object : Callback<UserDataItem> {

                    override fun onResponse(
                        call: Call<UserDataItem>,
                        response: Response<UserDataItem>
                    ) {
                        if (response.isSuccessful) {
                         //finish() // Move back to DestinationListActivity
                         //var newlyCreatedDestination = response.body() // Use it or ignore it
                            binding.neispravnoContainer.helperText = null
                        }
                        else {
                            binding.neispravnoContainer.helperText = "Neispravan email ili lozinka."
                        }
                        println("Uspesan zahtev")
                    }

                    override fun onFailure(call: Call<UserDataItem>, t: Throwable) {
                        println("Neuspesan zahtev")
                    }
                })

            }
        }
    }

    private fun submitForm(): Int
    {
        binding.emailContainer.helperText = validEmail()
        binding.passwordContainer.helperText = validPassword()

        val validEmail = binding.emailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null

        if(validEmail && validPassword)
            return 1
        else
            return -1
    }

    private fun emailFocusListener() {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                binding.emailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.emailEditText.text.toString()
        if(emailText.isEmpty()) {
            return "Unesite email!"
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.passwordEditText.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                binding.passwordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String?
    {
        val passwordText = binding.passwordEditText.text.toString()
        if(passwordText.isEmpty()) {
            return "Unesite lozinku!"
        }
        return null
    }
}