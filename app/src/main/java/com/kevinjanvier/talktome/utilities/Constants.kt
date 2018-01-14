package com.kevinjanvier.talktome.utilities

/**
 * Created by kevinjanvier on 29/12/2017.
 */
const val BASE_URL="https://talktomechat.herokuapp.com/v1/"
//const val BASE_URL="https://chattychatkevin.herokuapp.com/v1/"
//const val BASE_URL = "https://devslopes-chattin.herokuapp.com/v1/"
const val SOCKET_URL = "https://devslopes-chattin.herokuapp.com"

const val URL_REGISTER = "${BASE_URL}account/register"
const val URL_LOGIN = "${BASE_URL}account/login"
const val URL_CREATE_USER = "${BASE_URL}user/add"
const val URL_GET_USER = "${BASE_URL}user/byEmail/"
const val URL_GET_CHANNELS = "${BASE_URL}channel/"
const val URL_GET_MESSAGES = "${BASE_URL}message/byChannel/"


// Broadcast Constants
const val BROADCAST_USER_DATA_CHANGE = "BROADCAST_USER_DATA_CHANGE"
