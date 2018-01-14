package com.kevinjanvier.talktome.controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.kevinjanvier.talktome.R
import com.kevinjanvier.talktome.services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginProgress.visibility = View.INVISIBLE
    }

    fun loginButtonClick(view: View) {
        enableSpinner(true)
        val email = emailLoginText.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyboard()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserbyEmail(this) { findSuccess ->
                        if (findSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_LONG).show()
        }
    }

    fun signUpClick(view: View) {
        startActivity(Intent(this, CreateUserActivity::class.java))
        finish()
    }

     fun enableSpinner(enable: Boolean) {
        if (enable) {
            loginProgress.visibility = View.VISIBLE
        } else {
            loginProgress.visibility = View.INVISIBLE
        }

        loginBtn.isEnabled = !enable
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong !", Toast.LENGTH_SHORT).show()
        enableSpinner(false)
    }

    /**
     * Function that hide a keyboard
     */
    fun hideKeyboard(){

        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken,0)
        }
    }
}
