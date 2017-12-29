package com.kevinjanvier.talktome.controller

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kevinjanvier.talktome.R
import com.kevinjanvier.talktome.services.AuthService
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor ="[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
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

    fun createUserClicked(view :View){

        AuthService.registerUser(this, "email@gmail.com", "12345"){
            complete->
            if (complete == true){

                print("SUccess $complete")
            }else{
                print("Errror $complete")

            }
        }
    }
}
