package com.kevinjanvier.talktome.services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kevinjanvier.talktome.utilities.URL_REGISTER
import org.json.JSONObject

/**
 * Created by kevinjanvier on 29/12/2017.
 */
object AuthService {

    fun registerUser(context: Context, email:String, password:String, complete:(Boolean)->Unit){

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest = object :StringRequest(Request.Method.POST, URL_REGISTER, Response.Listener {
            response ->
            println(response)
            complete(true)

        }, Response.ErrorListener {
            error ->
            Log.d("Error", "Couldn't register User : $error")
            complete(false)
            
        }){
            override fun getBodyContentType(): String {
                return "application/json:charset=utf-8"
            }


            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }


        Volley.newRequestQueue(context).add(registerRequest)
    }
}