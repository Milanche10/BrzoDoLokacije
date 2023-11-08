package com.example.frontend

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    var saveButton: Button? = null
    var oldPassword: TextInputEditText? = null
    var newPassword: TextInputEditText? = null
    var confirmPassword: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        saveButton = findViewById(R.id.btn_save_changes)

        confPassFocusListener()
        passwordFocusListener()

        saveButton!!.setOnClickListener{

            if(findViewById<TextInputEditText>(R.id.newpasswordEditText).text.toString() != "" && (findViewById<TextInputEditText>(R.id.newpasswordEditText).text.toString() == findViewById<TextInputEditText>(R.id.confirmpasswordEditText).text.toString())) {

                if (submitForm() == 1) {
                    var context: Context = applicationContext;
                    val session = SessionManager(context)

                    var newUser = UserDataItem(
                        session.getId(),
                        null,
                        null,
                        session.getEmail(),
                        findViewById<TextInputEditText>(R.id.newpasswordEditText).text.toString(),
                        null
                    );

                    val apiInterface: ApiInterface =
                        ServiceBuilder.buildService(ApiInterface::class.java)
                    val requestCall: Call<UserInfoItem> = apiInterface.updateUserPassword(newUser)

                    requestCall.enqueue(object : Callback<UserInfoItem> {

                        override fun onResponse(
                            call: Call<UserInfoItem>,
                            response: Response<UserInfoItem>
                        ) {
                            if (response.isSuccessful) {

                                val responseBody = response.body()
                                Toast.makeText(
                                    this@ChangePasswordActivity,
                                    "Uspesno ste izmenili lozinku.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@ChangePasswordActivity, HomeActivity::class.java)
                                startActivity(intent)
                                println("Uspesan zahtev")
                            }
                        }

                        override fun onFailure(call: Call<UserInfoItem>, t: Throwable) {
                            println("Neuspesan zahtev")
                        }
                    })
                }
            }
            else if(findViewById<TextInputEditText>(R.id.newpasswordEditText).text.toString() != findViewById<TextInputEditText>(R.id.confirmpasswordEditText).text.toString())
            {
                if(findViewById<TextInputEditText>(R.id.confirmpasswordEditText).text.toString() == "")
                {
                    findViewById<TextView>(R.id.confirmPass)?.text = "Ovo polje je obavezno";
                }
                else
                    findViewById<TextView>(R.id.confirmPass)?.text = "Lozinke se ne poklapaju";
            }
        }
    }

    private fun submitForm(): Int {

        findViewById<TextView>(R.id.confirmPass)?.text = validConfPassword()
        findViewById<TextView>(R.id.newPass)?.text = validPassword()

        val validConPass = findViewById<TextView>(R.id.confirmPass)?.text == ""
        val newPass = findViewById<TextView>(R.id.newPass)?.text == ""

        if(validConPass && newPass)
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

    // PASSWORD
    private fun passwordFocusListener()
    {
        findViewById<TextInputEditText>(R.id.newpasswordEditText)?.setOnFocusChangeListener { _, focused ->

            if(!focused)
            {
                findViewById<TextView>(R.id.newPass)?.text = validPassword()
            }
        }
    }
    private fun validPassword(): String?
    {
        val passwordText = findViewById<TextInputEditText>(R.id.newpasswordEditText)?.text.toString()
        if(passwordText.isEmpty())
        {
            return "Ovo polje je obavezno"
        }
        if(passwordText.length in 1..7)
        {
            return "Minimum 8 karaktera"
        }
        if(!passwordText.matches(regex = "^(?!.* )(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,60}\$".toRegex())){

            return "Mora sadrzati 1 veliko slovo, 1 malo slovo i 1 broj"
        }


        return null;
    }

    private fun confPassFocusListener()
    {
        findViewById<TextInputEditText>(R.id.confirmpasswordEditText)?.setOnFocusChangeListener { _, focused ->

            if(!focused)
            {
                findViewById<TextView>(R.id.confirmPass)?.text = validConfPassword()
            }
        }
    }
    private fun validConfPassword(): String?
    {
        val passwordText = findViewById<TextInputEditText>(R.id.confirmpasswordEditText)?.text.toString()
        if(passwordText.isEmpty())
        {
            return "Ovo polje je obavezno"
        }
        if(passwordText != findViewById<TextInputEditText>(R.id.newpasswordEditText)?.text.toString())
        {
            return "Lozinke se ne poklapaju"
        }


        return null;
    }
}