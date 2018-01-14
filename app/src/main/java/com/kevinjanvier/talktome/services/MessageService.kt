package com.kevinjanvier.talktome.services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.kevinjanvier.talktome.controller.App
import com.kevinjanvier.talktome.model.Channel
import com.kevinjanvier.talktome.model.Message
import com.kevinjanvier.talktome.utilities.URL_GET_CHANNELS
import com.kevinjanvier.talktome.utilities.URL_GET_MESSAGES
import org.json.JSONException

/**
 * Created by kevinjanvier on 14/01/2018.
 */
object MessageService {
    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(complete: (Boolean) -> Unit) {
        val channelsRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->

            try {
                for (x in 0 until response.length()) {
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val chanDesc = channel.getString("description")
                    val channelId = channel.getString("_id")

                    val newChannel = Channel(name, chanDesc, channelId)
                    this.channels.add(newChannel)
                }
                complete(true)


            } catch (e: JSONException) {
                Log.e("JSON", "EXC " + e.localizedMessage)
                complete(false)
            }


        }, Response.ErrorListener {
            Log.d("ERROR", "Could not retrieve Channel")
            complete(false)

        }) {
            override fun getBodyContentType(): String {
                return "application/json: charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(channelsRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {
        val url = "$URL_GET_MESSAGES$channelId"
        val messageRequest = object : JsonArrayRequest(Method.GET, url, null,
                Response.Listener { response ->
                    clearMessages()
                    try {
                        for (x in 0 until response.length()) {
                            val message = response.getJSONObject(x)
                            val messagebody = message.getString("messageBody")
                            val channelId = message.getString("channelId")
                            val Id = message.getString("_id")
                            val userName = message.getString("userName")
                            val userAvatar = message.getString("userAvatar")
                            val userAvatarColor = message.getString("userAvatarColor")
                            val timeStamp = message.getString("timeStamp")

                            val newMessage = Message(messagebody, userName, channelId, userAvatar
                                    , userAvatarColor, Id, timeStamp)
                            this.messages.add(newMessage)
                        }
                        complete(true)

                    } catch (e: JSONException) {
                        Log.e("JSON", "EXC " + e.localizedMessage)
                        complete(false)

                    }
                }, Response.ErrorListener {
                        Log.d("ERROR", "Could not retrieve channels")
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
        App.prefs.requestQueue.add(messageRequest)
    }

    fun clearMessages() {
        messages.clear()
    }

    fun clearChannel() {
        channels.clear()
    }
}