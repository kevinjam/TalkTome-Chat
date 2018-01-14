package com.kevinjanvier.talktome.services

import android.graphics.Color
import com.kevinjanvier.talktome.controller.App
import java.util.*

/**
 * Created by kevinjanvier on 29/12/2017.
 */
object UserDataService {
    var id =""
    var name =""
    var avatarColor =""
    var avatarName =""
    var email =""
    var password =""

    fun returnAvatar(components:String):Int{
        //
        val strippedcolor = components
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")

        var r  =0
        var g = 0
        var b = 0
        val scanner = Scanner(strippedcolor)
        if (scanner.hasNext()){
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()

        }

        return Color.rgb(r,g,b)
    }


    fun logout(){
        var id =""
        var avatarColor =""
        var avatarName =""
        var email =""
        var name =""
        App.prefs.authToken = ""
        App.prefs.userEmail =""
        App.prefs.islogginIn = false
    }
}