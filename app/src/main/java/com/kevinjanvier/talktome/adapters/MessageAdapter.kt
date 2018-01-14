package com.kevinjanvier.talktome.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kevinjanvier.talktome.R
import com.kevinjanvier.talktome.model.Message
import com.kevinjanvier.talktome.services.UserDataService
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kevinjanvier on 14/01/2018.
 */
class MessageAdapter(val context: Context, val messages:ArrayList<Message>) :RecyclerView.Adapter<MessageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_listview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.binMessage(context, messages[position])
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        val userImage = itemView?.findViewById<ImageView>(R.id.messageuserImage)
        val timeStamp = itemView?.findViewById<TextView>(R.id.timestamplabel)
        val messageUsername = itemView?.findViewById<TextView>(R.id.messageusernameLabel)
        val messagebody = itemView?.findViewById<TextView>(R.id.messagebodylabel)

        fun binMessage(context: Context, message: Message){
            val resourceId = context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            userImage?.setImageResource(resourceId)
            userImage?.setBackgroundColor(UserDataService.returnAvatar(message.userAvatarColor))
            messageUsername?.text = message.userName
            timeStamp?.text = returnDateString(message.timeStamp)
            messagebody?.text = message.message
        }

        fun returnDateString(isoString: String) : String{

            //Monday 4
            val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormatter.timeZone = TimeZone.getTimeZone("UTC")

            var convertDate = Date()

            try {
                convertDate = isoFormatter.parse(isoString)

            }catch (e:ParseException){
                Log.d("PARSE ","cannot parse Date ")
            }
            val outDateString = SimpleDateFormat("E h:mm a", Locale.getDefault())
            return outDateString.format(convertDate)

        }

    }
}