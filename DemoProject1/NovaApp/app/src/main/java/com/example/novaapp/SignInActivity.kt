package com.example.novaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.Console

class SignInActivity : AppCompatActivity() {

    var et_email: EditText? = null
    var et_password: EditText? = null
    var sign_in_btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        et_email = findViewById(R.id.email)
        et_password = findViewById(R.id.password)

        sign_in_btn = findViewById(R.id.sign_in_btn)

        sign_in_btn?.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View?) {
                authenticateUser()
            }
        })
    }

    fun authenticateUser() {
        if (!validateEmail() || !validatePassword()) {
            return
        }
        //End Of Check For Errors

        //Instantiate The Request Queue;
        val queue: RequestQueue = Volley.newRequestQueue(this@SignInActivity)
        //The URL Posting TO:
        val url = "http://192.168.0.17:9080/api/v1/user/login"

        //Set Parameters
        val params: java.util.HashMap<String, String> = java.util.HashMap<String, String>()
        params.put("email", et_email?.getText().toString())
        params.put("password", et_password?.getText().toString())

        // Set Request Object;
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, JSONObject(params as Map<*, *>?),
            Response.Listener<JSONObject?>() {
                response->
                    try {
                        // Get Values From Response Object:
                        val first_name = response.get("first_name").toString()
                        val last_name = response.get("last_name") as String
                        val email = response.get("email") as String
                        //Set Intent Actions:
                        val goToProfile = Intent(this@SignInActivity, ProfileActivity::class.java)
                        //Pass Values To Profile Activity:
                        goToProfile.putExtra("first_name", first_name)
                        goToProfile.putExtra("last_name", last_name)
                        goToProfile.putExtra("email", email)

                        //Start Activity:
                        startActivity(goToProfile)
                        //finish()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
            }, object : Response.ErrorListener {
                @Override
                override fun onErrorResponse(error: VolleyError?) {
                    if (error != null) {
                        error.printStackTrace()
                    }
                    Toast.makeText(this@SignInActivity, "Login Failed", Toast.LENGTH_LONG).show()
                }
            }) //End Of Set Request Object
        queue.add(jsonObjectRequest)
    }

    fun goToHome(view: View?) {
        val intent = Intent(this@SignInActivity, MainActivity::class.java)
        startActivity(intent)
    }

    fun goToSignUpAct(view: View?) {
        val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun validateEmail(): Boolean {
        val email_e: String = et_email?.getText().toString()

        //Check If Email is Empty:
        return if (email_e.isEmpty()) {
            et_email?.setError("Email cannot be empty!")
            false
        } /*
        else if(StringHelper.regexEmailValidationPattern(email_e)) {
            email.setError("Please enter a valid email");
            return false;
        }*/ else {
            et_email?.setError(null)
            true
        } //Check If Email Is Empty
    }

    //End of Validate Email Field.
    fun validatePassword(): Boolean {
        val password_p: String = et_password?.getText().toString()

        //Check If First Name is Empty:
        return if (password_p.isEmpty()) {
            et_password?.setError("Password cannot be empty!")
            false
        } else {
            et_password?.setError(null)
            true
        } //Check If Password And Confirm Is Empty
    } //End of Validate Last Name Field.

}