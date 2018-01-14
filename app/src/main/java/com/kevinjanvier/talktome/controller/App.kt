package com.kevinjanvier.talktome.controller

import android.app.Application
import com.kevinjanvier.talktome.utilities.SharedPrefs

/**
 * Created by kevinjanvier on 14/01/2018.
 */
class App: Application() {
    companion object {
        lateinit var prefs:SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}