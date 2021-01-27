package com.example.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        verifyLoggedUser()
        setListeners();
        observe()
    }

    override fun onClick(v: View) {
        when (v.id) {
            button_login.id -> handleLogin()

            text_register.id -> startActivity(Intent(this, RegisterActivity::class.java))

            else -> Log.d("OnClickError", "element is not assigned to any onClick callbacks")
        }
    }


    private fun setListeners() {
        button_login.setOnClickListener(this)
        text_register.setOnClickListener(this)
    }

    private fun verifyLoggedUser() {
        mViewModel.verifyLoggedUser()

    }

    private fun observe() {
        mViewModel.loginValidation.observe(this, Observer {
            if (!it.getStatus()) {
                Toast.makeText(this, it.getMsg(), Toast.LENGTH_LONG)
                    .show()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

        mViewModel.verifyLogged.observe(this, Observer {
            if(it){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

    }

    private fun handleLogin() {
        val email = edit_email.text.toString()
        val password = edit_password.text.toString()

        mViewModel.doLogin(email, password)
    }

}
