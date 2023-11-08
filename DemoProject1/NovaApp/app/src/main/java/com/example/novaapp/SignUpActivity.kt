package com.example.novaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class SignUpActivity : AppCompatActivity() {
    var first_name: EditText? = null
    var last_name: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    var confirm: EditText? = null
    var sign_up_btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Hook Edit Text Fileds:
        first_name = findViewById(R.id.first_name)
        last_name = findViewById(R.id.last_name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirm = findViewById(R.id.confirm)

        //Hook Sign Up Button:
        sign_up_btn = findViewById(R.id.sign_up_btn)
        sign_up_btn?.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View) {
                processFormFields()
            }
        })
    }
    fun goToHome(view: View?) {
        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
        startActivity(intent)
    }

    fun goToSignInAct() {
        val intent = Intent(this@SignUpActivity, ProfileActivity::class.java)
        startActivity(intent)
    }


    fun processFormFields() {
        //Check For Errors
        if (!validateFirstName() || !validateLastName() || !validateEmail() || !validatePasswordAndConfirm()) {
            return
        }
        //End Of Check For Errors

        //Instantiate The Request Queue;
        val queue: RequestQueue = Volley.newRequestQueue(this@SignUpActivity)
        //The URL Posting TO:
        val url = "http://192.168.0.17:9080/api/v1/user/register"
        var flag = 0;
        //String Request Object:
        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, url, Response.Listener<String?> {
                response->
                if (response.equals("success")) {
                    first_name?.setText(null)
                    last_name?.setText(null)
                    email?.setText(null)
                    password?.setText(null)
                    confirm?.setText(null)
                    Toast.makeText(this@SignUpActivity, "Registration Successful", Toast.LENGTH_LONG).show()
                    goToSignInAct()
                }
            }, Response.ErrorListener {
                    //error.printStackTrace()
                    Toast.makeText(this@SignUpActivity, "Registration Un-Successful", Toast.LENGTH_LONG).show()
            }) {

                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["first_name"] = first_name?.getText().toString()
                    params["last_name"] = last_name?.getText().toString()
                    params["email"] = email?.getText().toString()
                    params["password"] = password?.getText().toString()

                    return params
                }

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Content-Type"] = "application/x-www-form-urlencoded"
                    return params
                }

            } // End Of String Request Object:

        queue.add(stringRequest)
    }

    //End Of Process Form Fields Method.
    fun validateFirstName(): Boolean {
        val firstName: String = first_name?.getText().toString()

        //Check If First Name is Empty:
        return if (firstName.isEmpty()) {
            first_name?.setError("First name cannot be empty!")
            false
        } else {
            first_name?.setError(null)
            true
        } //Check If First Name Is Empty
    }

    //End of Validate First Name Field.
    fun validateLastName(): Boolean {
        val lastName: String = last_name?.getText().toString()

        //Check If First Name is Empty:
        return if (lastName.isEmpty()) {
            last_name?.setError("Last name cannot be empty!")
            false
        } else {
            last_name?.setError(null)
            true
        } //Check If First Name Is Empty
    }

    //End of Validate Last Name Field.
    fun validateEmail(): Boolean {
        val email_e: String = email?.getText().toString()

        //Check If Email is Empty:
        return if (email_e.isEmpty()) {
            email?.setError("Email cannot be empty!")
            false
        } /*
        else if(StringHelper.regexEmailValidationPattern(email_e)) {
            email.setError("Please enter a valid email");
            return false;
        }*/ else {
            email?.setError(null)
            true
        } //Check If Email Is Empty
    }

    //End of Validate Email Field.
    fun validatePasswordAndConfirm(): Boolean {
        val password_p: String = password?.getText().toString()
        val confirm_p: String = confirm?.getText().toString()

        //Check If First Name is Empty:
        return if (password_p.isEmpty() || confirm_p.isEmpty()) {
            password?.setError("Password cannot be empty!")
            confirm?.setError("Confirm filed cannot be empty!")
            false
        } else if (!password_p.equals(confirm_p)) {
            password?.setError("Passwords do not much!")
            false
        } else if (confirm_p.isEmpty()) {
            confirm?.setError("Confirm filed cannot be empty!")
            false
        } else {
            password?.setError(null)
            confirm?.setError(null)
            true
        } //Check If Password And Confirm Is Empty
    } //End of Validate Last Name Field.

}