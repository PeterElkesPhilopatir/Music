package com.peter.music.service

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.peter.music.BuildConfig.BASE_URL
import com.peter.music.Constant.QUERY
import com.peter.music.Constant.SEARCH
import com.peter.music.HeadersKey.AUTHORIZATION
import com.peter.music.HeadersValue.BEARER
import com.peter.music.pojo.Track
import com.peter.music.service.Utilities.getStringResponse
import com.peter.music.service.Utilities.gsonify
import com.peter.music.service.Utilities.setDefaultHttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.HttpRetryException
import java.net.HttpURLConnection
import java.net.URL
import com.peter.music.service.responses.TrackResponse
import com.peter.music.service.parseToPlainObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SearchSource {
    suspend fun searchForTrack(
        query: String
    ): Flow<List<Track>>
}

class SearchSourceImpl @Inject constructor(
    private val token: TokenSource,
    private val auth: Auth
) : SearchSource {
    override suspend fun searchForTrack(query: String) =
        flow {
            val queryParam = "?$QUERY=$query"
            val getMusicUrl = "$BASE_URL$SEARCH$queryParam"
            val url = URL(getMusicUrl)
            val httpConnection = url.openConnection() as HttpURLConnection
            try {
                val token = token.getToken()
                httpConnection.requestMethod = "GET"
                httpConnection.setRequestProperty(
                    AUTHORIZATION,
                    "$BEARER $token"
                )

                setDefaultHttpHeaders(httpConnection)
                httpConnection.connect()
                Log.d("SearchSourceImpl", httpConnection.responseMessage)
                when (httpConnection.responseCode) {
                    HttpURLConnection.HTTP_OK -> {
                        val responseList = paresMusicList(httpConnection)
                        emit(responseList.filter { it.title != null }
                            .map { it.parseToPlainObject() })
                    }
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        auth.authenticate()
                        throw HttpRetryException(
                            httpConnection.responseMessage,
                            HttpURLConnection.HTTP_UNAUTHORIZED
                        )
                    }

                }

            } catch (throwable: Throwable) {
                throw throwable
            } finally {
                httpConnection.disconnect()
            }


        }.flowOn(Dispatchers.IO)

    private fun paresMusicList(connection: HttpURLConnection): List<TrackResponse> {
        val listType = object : TypeToken<List<TrackResponse>>() {}.type
        return gsonify().fromJson(connection.getStringResponse(), listType) as List<TrackResponse>
    }
}
