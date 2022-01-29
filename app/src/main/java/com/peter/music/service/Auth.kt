package com.peter.music.service


import android.util.Log
import com.google.gson.GsonBuilder
import com.peter.music.Constant.TOKEN
import com.peter.music.*
import java.net.HttpURLConnection
import java.net.URL
import com.peter.music.BuildConfig.*
import com.peter.music.HeadersKey.ACCEPT
import com.peter.music.HeadersKey.CONTENT_TYPE
import com.peter.music.HeadersKey.GATEWAY_KEY
import com.peter.music.HeadersValue.APPLICATION_X_WWWW_FORM
import com.peter.music.HeadersValue.APP_JSON
import java.net.HttpURLConnection.HTTP_OK
import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.peter.music.service.Utilities.getStringResponse
import com.peter.music.service.Utilities.gsonify
import com.peter.music.service.Utilities.setHeaders
import com.peter.music.service.responses.Authentication
import java.net.Authenticator
import javax.inject.Inject

interface Auth {
    fun authenticate()
}

class AuthImpl @Inject constructor(private val token: TokenSource) :
    Auth {
    override fun authenticate() {
        val getTokenUrl = "$BASE_URL$TOKEN"
        val url = URL(getTokenUrl)
        val httpConnection = url.openConnection() as HttpURLConnection
        httpConnection.requestMethod = "POST"
        httpConnection.setRequestProperty(GATEWAY_KEY, GATEWAY_KEY_VALUE)
        setHeaders(httpConnection)
        httpConnection.connect()
        Log.i("Auth", httpConnection.responseMessage)
        Log.i("Auth", httpConnection.responseCode.toString())
        if (httpConnection.responseCode == HTTP_OK) {
            parseToken(httpConnection)?.accessToken?.let {
                token.saveToken(it)
            }
        }
        httpConnection.disconnect()
    }

    private fun parseToken(connection: HttpURLConnection) =
        gsonify().fromJson(connection.getStringResponse(), Authentication::class.java)

}