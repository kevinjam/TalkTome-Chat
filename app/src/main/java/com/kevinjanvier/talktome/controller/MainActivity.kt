package com.kevinjanvier.talktome.controller

import android.content.*
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import com.kevinjanvier.talktome.R
import com.kevinjanvier.talktome.model.Channel
import com.kevinjanvier.talktome.services.AuthService
import com.kevinjanvier.talktome.services.MessageService
import com.kevinjanvier.talktome.services.UserDataService
import com.kevinjanvier.talktome.utilities.BROADCAST_USER_DATA_CHANGE
import com.kevinjanvier.talktome.utilities.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    lateinit var channelAdapter :ArrayAdapter<Channel>
    var selectedChannel :Channel?=null


    private fun setUpadapter(){
        channelAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, MessageService.channels)
        channel_list.adapter = channelAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        setUpadapter()


        channel_list.setOnItemClickListener{
            _, _, i, _ ->

            selectedChannel = MessageService.channels[i]
            drawer_layout.closeDrawer(GravityCompat.START)
            updatewithChannel()
        }


        if (App.prefs.islogginIn){
            AuthService.findUserbyEmail(this){}
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangedReceiver,
                IntentFilter(BROADCAST_USER_DATA_CHANGE))

    }

    private val userDataChangedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //when receive a broadCast
            if (App.prefs.islogginIn) {
                userNameNavHeader.text = UserDataService.name
                userEmailnavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatar(UserDataService.avatarColor))
                loginnavBtn.text = "Logout"

                MessageService.getChannels{
                    complete->
                    if (complete){
                        if (MessageService.channels.count() >0){
                            selectedChannel = MessageService.channels[0]
                            channelAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    fun updatewithChannel(){
        messagechannelLogin.text = "#${selectedChannel?.name}"
    }


    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangedReceiver,
                IntentFilter(BROADCAST_USER_DATA_CHANGE
        ))
        socket.connect()
    }

    override fun onRestart() {
        super.onRestart()
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangedReceiver,
                IntentFilter(BROADCAST_USER_DATA_CHANGE
        ))
        socket.connect()
    }

    override fun onDestroy() {
        socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangedReceiver)
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginNavClick(view: View) {
        if (App.prefs.islogginIn) {
            //logout
            UserDataService.logout()
            userNameNavHeader.text = ""
            userEmailnavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginnavBtn.text = "Login"
        } else {
            val loginActivty = Intent(this, LoginActivity::class.java)
            startActivity(loginActivty)
        }
    }

    fun addChannelClick(view: View) {
        if (App.prefs.islogginIn) {
            //show alert Dialog once login
            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.add_channel_dialog, null)
            builder.setView(dialogview)
                    .setPositiveButton("Add") { dialog, i ->
                        //perfom some logic when clicked

                        val nameTextrField = dialogview.findViewById<EditText>(R.id.addchannelNameText)
                        val descritpionField = dialogview.findViewById<EditText>(R.id.addchannelDescription)
                        val channelname= nameTextrField.text.toString()
                        val channelDesc = descritpionField.text.toString()

                        //create channel the desc
                        socket.emit("newChannel", channelname, channelDesc)

                    }
                    .setNegativeButton("Cancel") { dialog, i ->
                        //cancel and close the dialog
                    }
                    .show()
        }
    }
    
    private val onNewChannel = Emitter.Listener { args ->
        runOnUiThread{
            val channelName = args[0] as String
            val channelDescription = args[1] as String
            val channelId = args[2] as String

            val newChannel = Channel(channelName, channelDescription, channelId)
            MessageService.channels.add(newChannel)
            channelAdapter.notifyDataSetChanged()
            println(newChannel.name)
            println(newChannel.chanDesc)
            println(newChannel.channelId)
        }
    }

    fun sendMessageClick(view: View) {
        hideKeyboard()
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
