package com.kevinjanvier.talktome.services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.kevinjanvier.talktome.controller.App
import com.kevinjanvier.talktome.utilities.*
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by kevinjanvier on 29/12/2017.
 */
object AuthService {

//    var isLogin = false
//    var userEmail = ""
//    var authToken = ""

    fun registerUser(email:String, password:String, complete:(Boolean)->Unit){

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest = object :StringRequest(Request.Method.POST, URL_REGISTER, Response.Listener {
            response ->
            println("Register $response")
            complete(true)

        }, Response.ErrorListener {
            error ->
            Log.d("Error", "Couldn't register User : $error")
            complete(false)
            
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }


            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
//
//            override fun getHeaders(): MutableMap<String, String> {
//                return super.getHeaders()
//            }
        }

        App.prefs.requestQueue.add(registerRequest)

    }

    fun loginUser( email: String, password: String, complete: (Boolean) -> Unit){
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object :JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener {               response ->

        //this is where we parse our json object
           try {
               App.prefs.userEmail = response.getString("user")
               App.prefs.authToken = response.getString("token")
               App.prefs.islogginIn = true
               complete(true)
           }catch (e:JSONException){
               println("Error "+e.localizedMessage)

               complete(false)
           }


        }, Response.ErrorListener {
            //Deal with the error
            error ->
            Log.d("Error", "Couldn't register User : $error")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }
        App.prefs.requestQueue.add(loginRequest)

    }

    fun createUser(name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()

        val createRequest = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener { response ->

            try {

                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")
                complete(true)

            } catch (e: JSONException) {
                Log.d("JSON", "EXC " + e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not add user: ${error.toString()}")
            Log.e("ERRRR " ,"Bugs is here"+ error.message)

            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(createRequest)
    }

//    fun findUserbyEmail(context: Context, complete: (Boolean) -> Unit){
//        val findUserRequest = object :JsonObjectRequest(Method.GET, "$URL_CREATE_USER${App.prefs.userEmail}", null, Response.Listener {
////            response->
//            try {
//                UserDataService.name = it.getString("name")
//                UserDataService.email = it.getString("email")
//                UserDataService.avatarName = it.getString("avatarName")
//                UserDataService.avatarColor = it.getString("avatarColor")
//                UserDataService.id = it.getString("_id")
//
//                val userDatachange = Intent(BROADCAST_USER_DATA_CHANGE)
//                LocalBroadcastManager.getInstance(context).sendBroadcast(userDatachange)
//                complete(true)
//
//            }catch (e:JSONException){
//                Log.e("JSON", "EXC " +e.localizedMessage)
//            }
//
//
//
//        }, Response.ErrorListener { error ->
//            Log.e("ERROR", "Couln not find user")
//            complete(false)
//        }){
//            override fun getBodyContentType(): String {
//                return "application/json; charset=utf-8"
//            }
//
//            override fun getHeaders(): MutableMap<String, String> {
//                val headers = HashMap<String, String>()
//                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
//                return headers
//            }
//        }
//
//        App.prefs.requestQueue.add(findUserRequest)
//
//    }

    fun findUserbyEmail(context: Context, complete: (Boolean) -> Unit) {
        val findUserRequest = object: JsonObjectRequest(Method.GET, "$URL_GET_USER${App.prefs.userEmail}", null, Response.Listener { response ->

            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")

                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.localizedMessage)
            }

        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not find user.")
            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(findUserRequest)
    }
}