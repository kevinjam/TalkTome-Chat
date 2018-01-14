package com.kevinjanvier.talktome.controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.kevinjanvier.talktome.R
import com.kevinjanvier.talktome.services.AuthService
import com.kevinjanvier.talktome.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor ="[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        createSpinner.visibility = View.INVISIBLE
    }

    fun generateuserAvatar(view : View){
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)
        if (color== 0){
            userAvatar = "light$avatar"
        }else{
            userAvatar ="dark$avatar"
        }

        val resouldId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvator.setImageResource(resouldId)

    }


    fun generateColorClicked(view: View){
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvator.setBackgroundColor(Color.rgb(r,g,b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() /255

        avatarColor ="[$savedR, $savedG, $savedB"

    }


    fun createUserClicked(view: View) {
        enableSpinner(true)
        val userName = createUsername.text.toString()
        val email = createemail.text.toString()
        val password = createPassword.text.toString()

        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.registerUser(email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(email, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(userName, email, userAvatar, avatarColor) { createSuccess ->
                                if (createSuccess) {
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
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
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this, "Make sure user name, email, and password are filled in.",
                    Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }
    }

    fun enableSpinner(enable:Boolean){
        if (enable){
            createSpinner.visibility = View.VISIBLE
        }else{
            createSpinner.visibility = View.INVISIBLE
        }

        createUserbtn.isEnabled = !enable
        createAvator.isEnabled = !enable
        createColorbtn.isEnabled =!enable
    }

    fun errorToast(){
        Toast.makeText(this,"Something went wrong !", Toast.LENGTH_SHORT).show()
        enableSpinner(false)
    }
}
