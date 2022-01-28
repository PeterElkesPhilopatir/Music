package com.peter.music.service

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.peter.music.BuildConfig
import com.peter.music.HeadersKey
import com.peter.music.HeadersValue
import com.peter.music.pojo.Track
import com.peter.music.service.responses.TrackResponse
import java.net.HttpURLConnection
import kotlin.random.Random

object Utilities {

    fun setDefaultHttpHeaders(httpURLConnection: HttpURLConnection) {
        httpURLConnection.requestMethod = "POST"
        httpURLConnection.setRequestProperty(HeadersKey.GATEWAY_KEY, BuildConfig.GATEWAY_KEY_VALUE)
        httpURLConnection.setRequestProperty(HeadersKey.ACCEPT, HeadersValue.APP_JSON)
        httpURLConnection.setRequestProperty(
            HeadersKey.CONTENT_TYPE,
            HeadersValue.APPLICATION_X_WWWW_FORM
        )
    }

    fun gsonify() = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    fun HttpURLConnection.getStringResponse(): String {
        val response = StringBuffer()
        try {
            this.inputStream.bufferedReader().use { reader ->
                var input = ""
                while (reader.readLine().also { input = it } != null)
                    response.append(input)
            }
        } catch (throwable: Throwable) {
            Log.d("AuthenticatorImpl", throwable.message.toString())
        }

        return response.toString()
    }


}

fun TrackResponse.parseToPlainObject() = Track(
    id = this.id ?:  (1..5)
        .map { i -> Random.nextInt(0, 26+9) }
        .map("abcdefghijklmnopqrstuvwxyz123456789"::get)
        .joinToString(""),
    title = this.title ?: "",
    type = this.type ?: "",
    publishingDate = this.publishingDate?.split("T")?.get(0) ?: "",
    mainArtist = this.mainArtist?.name ?: "",
    image = "http:${this.cover?.small}",

    )



