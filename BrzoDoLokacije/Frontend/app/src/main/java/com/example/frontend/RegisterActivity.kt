package com.example.frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.frontend.databinding.ActivityMainBinding
import com.example.frontend.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    var first_name: EditText? = null
    var last_name: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    var sign_up_btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

       /* first_name = findViewById(R.id.first_nameEditText)
        last_name = findViewById(R.id.last_nameEditText)
        email = findViewById(R.id.emailEditText)
        password = findViewById(R.id.passwordEditText)
        */
        first_name = binding.FirstNameEditText
        last_name = binding.LastNameEditText
        email = binding.emailEditText
        password = binding.passwordEditText
        emailFocusListener()
        passwordFocusListener()
        firstNameFocusListener()
        lastNameFocusListener()

        sign_up_btn = findViewById(R.id.sign_up_btn)
        sign_up_btn?.setOnClickListener {
            if (submitForm() == 1) {
                var newUser = UserDataItem(
                    -1,
                    first_name!!.text.toString(),
                    last_name!!.text.toString(),
                    email!!.text.toString(),
                    password!!.text.toString()
                );


                val apiInterface: ApiInterface =
                    ServiceBuilder.buildService(ApiInterface::class.java)
                val requestCall: Call<UserDataItem> = apiInterface.addUser(newUser)

                requestCall.enqueue(object : Callback<UserDataItem> {

                    override fun onResponse(
                        call: Call<UserDataItem>,
                        response: Response<UserDataItem>
                    ) {
                        /* if (response.isSuccessful) {
                         finish() // Move back to DestinationListActivity
                         var newlyCreatedDestination = response.body() // Use it or ignore it

                     } else {

                     }*/
                        println("uspesno")
                    }

                    override fun onFailure(call: Call<UserDataItem>, t: Throwable) {
                        println("neuspesno")
                    }
                })
            }
            else
            {
                println("NIJE DOBRA FORMA")
            }
        }
    }

    // VALIDACIJA

    private fun submitForm(): Int {

        binding.emailContainer.helperText = validEmail()
        binding.passwordContainer.helperText = validPassword()
        binding.firstNameContainer.helperText = validFirstName()
        binding.lastNameContainer.helperText = validLastName()

        val validEmail = binding.emailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        val validFirstName = binding.firstNameContainer.helperText == null
        val validLastName = binding.lastNameContainer.helperText == null

        if(validEmail && validPassword && validFirstName && validLastName)
        {
            //validForm
            return 1;
        }
        else
        {
            //invalidForm
            return -1;
        }
    }


    // EMAIL
    private fun emailFocusListener()
    {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->

            if(!focused)
            {
                binding.emailContainer.helperText = validEmail()
            }
        }
    }
    private fun validEmail(): String?
    {
        val emailText = binding.emailEditText.text.toString()
        if(emailText.isEmpty())
        {
            return "Required"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Invalid Email Address"
        }


        return null;
    }

    // PASSWORD
    private fun passwordFocusListener()
    {
        binding.passwordEditText.setOnFocusChangeListener { _, focused ->

            if(!focused)
            {
                binding.passwordContainer.helperText = validPassword()
            }
        }
    }
    private fun validPassword(): String?
    {
        val passwordText = binding.passwordEditText.text.toString()
        if(passwordText.isEmpty())
        {
            return "Required"
        }
        if(passwordText.length in 1..7)
        {
            return "Minimum 8 Character Password"
        }
        if(!passwordText.matches(regex = "^(?!.* )(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,60}\$".toRegex())){

            return "Must contain 1 uppercase, 1 letter and 1 number"
        }


        return null;
    }

    // FIRSTNAME
    private fun firstNameFocusListener()
    {/*
        binding.FirstNameEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                binding.firstNameContainer.helperText = validFirstName()
            }
            false
        })*/

        binding.FirstNameEditText.setOnFocusChangeListener { _, focused ->

            if(!focused)
            {
                binding.firstNameContainer.helperText = validFirstName()
            }
        }
    }
    private fun validFirstName(): String?
    {
        val firstNameText = binding.FirstNameEditText.text.toString()
        if(firstNameText.isEmpty())
        {
            return "Required"
        }
        if(!firstNameText.matches(regex = "^[A-Za-z][A-Za-z ]+\$".toRegex())){

            return "Invalid Input"
        }


        return null;
    }

    // LASTNAME
    private fun lastNameFocusListener()
    {
        binding.LastNameEditText.setOnFocusChangeListener { _, focused ->

            if(!focused)
            {
                binding.lastNameContainer.helperText = validLastName()
            }
        }
    }
    private fun validLastName(): String?
    {
        val lastNameText = binding.LastNameEditText.text.toString()
        if(lastNameText.isEmpty())
        {
            return "Required"
        }
        if(!lastNameText.matches(regex = "^[A-Za-z][A-Za-z ]+\$".toRegex())){

            return "Invalid Input"
        }

        return null;
    }

    fun goToLogin(view: View?) {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}