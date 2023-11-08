package com.example.frontend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginTabFragment : Fragment() {

    private lateinit var session: SessionManager
    var email: EditText? = null
    var emailText: TextView? = null
    var password: EditText? = null
    var passwordText: TextView? = null
    var validationText: TextView? = null
    var sign_in_btn: Button? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        var view : View = inflater.inflate(R.layout.login_tab_fragment, container, false) as ViewGroup

        var context: Context = view.getContext();
        session = SessionManager(context)

        //Ako je vec ulogovan prebaci na stranu profil
        if(session.isLogin()!!) {
            var intent: Intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
        }

        email = view?.findViewById(R.id.email)
        password = view?.findViewById(R.id.pass)
        emailText = view?.findViewById(R.id.emailText)
        passwordText = view?.findViewById(R.id.passText)
        validationText = view?.findViewById(R.id.validationText)

        emailFocusListener()
        passwordFocusListener()

        sign_in_btn = view?.findViewById(R.id.button2)
        sign_in_btn?.setOnClickListener {

            if (submitForm() == 1) {
                var newUser = UserDataItem(
                    -1,
                    null,
                    null,
                    email!!.text.toString(),
                    password!!.text.toString(),
                    null
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

                            session.setLogin(true)
                            session.setToken(response.body()?.token.toString())
                            session.setEmail(email!!.text.toString())

                            email!!.text = null
                            password!!.text = null

                            var intent: Intent = Intent(context, HomeActivity::class.java)
                            startActivity(intent)

                        }
                        else {
                            validationText!!.text = "Neispravno korisnicko ime/lozinka."
                        }
                        println("Uspesan zahtev")

                    }

                    override fun onFailure(call: Call<UserDataItem>, t: Throwable) {
                        validationText!!.text = "Doslo je do greske. Pokusajte ponovo."
                        println("Neuspesan zahtev")
                    }
                })

            }
            else {
                println("Nije dobra forma za prijavu!")
            }
        }

        return view
    }


    private fun submitForm(): Int
    {
        emailText?.text = validEmail()
        passwordText?.text = validPassword()

        val validEmail = emailText?.text == ""
        val validPassword = passwordText?.text == ""

        if(validEmail && validPassword) {
            return 1;
        }
        else {
            return -1;
        }
    }

    private fun emailFocusListener()
    {
        email?.setOnFocusChangeListener { _, focused ->

            if(!focused) {
                emailText?.text = validEmail()
            }
        }
    }
    private fun validEmail(): String?
    {
        val emailText = email?.text.toString()
        if(emailText.isEmpty()) {
            return "Ovo polje je obavezno"
        }
        return null;
    }

    private fun passwordFocusListener()
    {
        password?.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                passwordText?.text = validPassword()
            }
        }
    }
    private fun validPassword(): String?
    {
        val passwordText = password?.text.toString()
        if(passwordText.isEmpty()) {
            return "Ovo polje je obavezno"
        }
        return null;
    }
}