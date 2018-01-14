package com.kevinjanvier.talktome.utilities

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.toolbox.Volley

/**
 * Created by kevinjanvier on 14/01/2018.
 */
class SharedPrefs (context: Context){
    val PREF_FILENAME="prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREF_FILENAME,0)

    val IS_LOGGED_IN ="isloggedIn"
    val AUTH_TOKEN ="authToken"
    val USER_EMAIL ="userEmail"

    var islogginIn :Boolean
    get() = prefs.getBoolean(IS_LOGGED_IN, false)
    set(value) = prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var authToken:String
    get() = prefs.getString(AUTH_TOKEN, "")
    set(value) = prefs.edit().putString(AUTH_TOKEN, value).apply()

    var userEmail : String
    get() = prefs.getString(USER_EMAIL, "")
    set(value) = prefs.edit().putString(USER_EMAIL, value).apply()

    val ewquesrQueue = Volley.newRequestQueue(context)
}