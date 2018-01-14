package com.kevinjanvier.talktome.services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.kevinjanvier.talktome.controller.App
import com.kevinjanvier.talktome.model.Channel
import com.kevinjanvier.talktome.utilities.URL_GET_CHANNELS
import org.json.JSONException

/**
 * Created by kevinjanvier on 14/01/2018.
 */
object MessageService {
    val channels = ArrayList<Channel>()

    fun getChannels(context: Context, complete:(Boolean) ->Unit){
        val channelsRequest = object :JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener {
            response ->

            try {
                for (x in 0 until response.length()){
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val chanDesc = channel.getString("description")
                    val channelId = channel.getString("_id")

                    val newChannel = Channel(name, chanDesc, channelId)
                    this.channels.add(newChannel)
                }
                complete(true)


            }catch (e:JSONException){
                Log.e("JSON", "EXC "+e.localizedMessage)
                complete(false)
            }


        }, Response.ErrorListener {
            Log.d("ERROR", "Could not retrieve Channel")
            complete(false)

        }){
            override fun getBodyContentType(): String {
                return "application/json: charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        Volley.newRequestQueue(context).add(channelsRequest)
    }
}