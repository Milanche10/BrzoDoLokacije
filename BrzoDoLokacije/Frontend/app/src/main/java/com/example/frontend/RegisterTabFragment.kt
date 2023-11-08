package com.example.frontend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View;
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterTabFragment: Fragment() {

    private lateinit var session: SessionManager
    var first_name: EditText? = null
    var last_name: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    var conf_pass: EditText? = null
    var first_namePoruka: TextView? = null
    var last_namePoruka: TextView? = null
    var emailPoruka: TextView? = null
    var passwordPoruka: TextView? = null
    var conf_passPoruka: TextView? = null
    var sign_up_btn: Button? = null
    var validationText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        var view : View = inflater.inflate(R.layout.register_tab_fragment, container, false) as ViewGroup

        var context: Context = view.getContext();
        session = SessionManager(context)

        first_name = view.findViewById(R.id.first_name)
        last_name = view.findViewById(R.id.last_name)
        email = view.findViewById(R.id.email)
        password = view.findViewById(R.id.password)
        conf_pass = view.findViewById(R.id.conf_pass)

        first_namePoruka = view.findViewById(R.id.first_namePoruka)
        last_namePoruka = view.findViewById(R.id.last_namePoruka)
        emailPoruka = view.findViewById(R.id.emailPoruka)
        passwordPoruka = view.findViewById(R.id.passwordPoruka)
        conf_passPoruka = view.findViewById(R.id.conf_passPoruka)
        validationText = view?.findViewById(R.id.validationText)

        emailFocusListener()
        passwordFocusListener()
        confPassFocusListener()
        firstNameFocusListener()
        lastNameFocusListener()

        sign_up_btn = view.findViewById(R.id.sign_up_btn)
        sign_up_btn?.setOnClickListener {
            if (submitForm() == 1) {
                var newUser = UserDataItem(
                    -1,
                    first_name!!.text.toString(),
                    last_name!!.text.toString(),
                    email!!.text.toString(),
                    password!!.text.toString(),
                    null
                );


                val apiInterface: ApiInterface =
                    ServiceBuilder.buildService(ApiInterface::class.java)
                val requestCall: Call<UserDataItem> = apiInterface.addUser(newUser)

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
                            var context: Context = view.getContext();
                            var intent: Intent = Intent(context, HomeActivity::class.java)
                            startActivity(intent)

                        } else {
                            validationText!!.text = "Uneti email je zauzet."
                        }
                        println("uspesno")
                    }

                    override fun onFailure(call: Call<UserDataItem>, t: Throwable) {
                        validationText!!.text = "Doslo je do greske. Pokusajte ponovo."
                        println("neuspesno")
                    }
                })
            }
            else
            {
                println("nije dobra forma")
                validationText!!.text = "Popunite sva polja."
            }
        }

        return view;

    }

    // VALIDACIJA

    private fun submitForm(): Int {

        emailPoruka?.text = validEmail()
        passwordPoruka?.text = validPassword()
        first_namePoruka?.text = validFirstName()
        last_namePoruka?.text = validLastName()
        conf_passPoruka?.text = validConfPassword()

        val validEmail = emailPoruka?.text == ""
        val validPassword = passwordPoruka?.text == ""
        val validFirstName = first_namePoruka?.text == ""
        val validLastName = last_namePoruka?.text == ""
        val validConPass = conf_passPoruka?.text == ""

        if(validEmail && validPassword && validFirstName && validLastName && validConPass)
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
        email?.setOnFocusChangeListener { _, focused ->

            if(!focused) {
                emailPoruka?.text = validEmail()
            }
        }
    }
    private fun validEmail(): String?
    {
        val emailText = email?.text.toString()
        if(emailText.isEmpty())
        {
            return "Ovo polje je obavezno"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Neispravan format"
        }


        return null;
    }

    // PASSWORD
    private fun passwordFocusListener()
    {
        password?.setOnFocusChangeListener { _, focused ->

            if(!focused)
            {
                passwordPoruka?.text = validPassword()
            }
        }
    }
    private fun validPassword(): String?
    {
        val passwordText = password?.text.toString()
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

    // CONF PASS
    private fun confPassFocusListener()
    {
        conf_pass?.setOnFocusChangeListener { _, focused ->

            if(!focused)
            {
                conf_passPoruka?.text = validConfPassword()
            }
        }
    }
    private fun validConfPassword(): String?
    {
        val passwordText = conf_pass?.text.toString()
        if(passwordText.isEmpty())
        {
            return "Ovo polje je obavezno"
        }
        if(passwordText.equals(password?.text.toString()) == false)
        {
            return "Lozinke se ne poklapaju"
        }


        return null;
    }

    // FIRSTNAME
    private fun firstNameFocusListener()
    {
        first_name?.setOnFocusChangeListener { _, focused ->

            if(!focused)
            {
                first_namePoruka?.text = validFirstName()
            }
        }
    }
    private fun validFirstName(): String?
    {
        val firstNameText = first_name?.text.toString()
        if(firstNameText.isEmpty())
        {
            return "Ovo polje je obavezno"
        }
        if(!firstNameText.matches(regex = "^[A-Za-z][A-Za-z ]+\$".toRegex())){

            return "Neispravan format"
        }


        return null;
    }

    // LASTNAME
    private fun lastNameFocusListener()
    {
        last_name?.setOnFocusChangeListener { _, focused ->

            if(!focused)
            {
                last_namePoruka?.text = validLastName()
            }
        }
    }
    private fun validLastName(): String?
    {
        val lastNameText = last_name?.text.toString()
        if(lastNameText.isEmpty())
        {
            return "Ovo polje je obavezno"
        }
        if(!lastNameText.matches(regex = "^[A-Za-z][A-Za-z ]+\$".toRegex())){

            return "Neispravan format"
        }

        return null;
    }

}
