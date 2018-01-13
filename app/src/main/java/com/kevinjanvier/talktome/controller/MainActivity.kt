package com.kevinjanvier.talktome.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kevinjanvier.talktome.R
import com.kevinjanvier.talktome.services.AuthService
import com.kevinjanvier.talktome.services.UserDataService
import com.kevinjanvier.talktome.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangedReceiver,
                IntentFilter(BROADCAST_USER_DATA_CHANGE))

    }


    private val userDataChangedReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            //when receive a broadCast
            if (AuthService.isLogin){
                userNameNavHeader.text = UserDataService.name
                userEmailnavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatar(UserDataService.avatarColor))
                loginnavBtn.text ="Logout"
            }

        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
fun loginNavClick(view: View){
    if (AuthService.isLogin){
        //logout
        UserDataService.logout()
        userNameNavHeader.text = "Login"
        userEmailnavHeader.text =""
        userImageNavHeader.setImageResource(R.drawable.profiledefault)
        userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
        loginnavBtn.text = "Login"

    }else{
        val loginActivty=Intent(this, LoginActivity::class.java)
        startActivity(loginActivty)
    }


}

    fun addChannelClick(view:View){

    }

    fun sendMessageClick(view: View){

    }


}
