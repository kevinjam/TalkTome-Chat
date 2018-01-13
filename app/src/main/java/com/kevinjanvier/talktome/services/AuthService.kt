package com.kevinjanvier.talktome.services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kevinjanvier.talktome.utilities.URL_CREATE_USER
import com.kevinjanvier.talktome.utilities.URL_LOGIN
import com.kevinjanvier.talktome.utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by kevinjanvier on 29/12/2017.
 */
object AuthService {

    var isLogin = false
    var userEmail = ""
    var authToken = ""

    fun registerUser(context: Context, email:String, password:String, complete:(Boolean)->Unit){

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

            override fun getHeaders(): MutableMap<String, String> {
                return super.getHeaders()
            }
        }

        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object :JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener {               response ->

        //this is where we parse our json object
           try {
               userEmail = response.getString("user")
               authToken = response.getString("token")
               isLogin = true
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
        Volley.newRequestQueue(context).add(loginRequest)
    }

    fun createUser(context: Context, name:String, email:String, avatarName:String, avatarColor:String, complete: (Boolean) -> Unit){

        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()

        var createRequest = object :JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener {response->

            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")
                complete(true)
            }catch (e:JSONException){
                Log.e("JSON", "EXC" +e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener {error ->
            Log.e("ERROR", "Could not Create user $error")


        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(createRequest)
    }
}