package com.peter.music.service

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.peter.music.BuildConfig.BASE_URL
import com.peter.music.Constant.ACCESSTOKEN
import com.peter.music.Constant.QUERY
import com.peter.music.Constant.SEARCH
import com.peter.music.HeadersKey.AUTHORIZATION
import com.peter.music.HeadersValue.BEARER
import com.peter.music.pojo.Track
import com.peter.music.service.Utilities.getStringResponse
import com.peter.music.service.Utilities.gsonify
import com.peter.music.service.Utilities.setHeaders
import java.net.HttpRetryException
import java.net.HttpURLConnection
import java.net.URL
import com.peter.music.service.business.responses.TrackResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
            val token = token.getToken()
            val queryParam = "?$QUERY=$query"
            val accessParam = "&$ACCESSTOKEN=$BEARER $token"
            val tracksUrl = "$BASE_URL$SEARCH$queryParam"

            val url = URL(tracksUrl)
            Log.i("url", url.toString())
            val httpConnection = url.openConnection() as HttpURLConnection
            try {
                httpConnection.requestMethod = "GET"
                httpConnection.setRequestProperty(
                    AUTHORIZATION,
                    "$BEARER $token"
                )
                setHeaders(httpConnection)
                httpConnection.connect()
                Log.d("SearchSourceImplCode", httpConnection.responseCode.toString())
                when (httpConnection.responseCode) {
                    HttpURLConnection.HTTP_OK -> {
                        val responseList = parseTracksList(httpConnection)
                        emit(responseList.filter { it.title != null }.map { it.parseToPlainObject() })
                    }
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        auth.authenticate()
                        throw HttpRetryException(httpConnection.responseMessage,
                            HttpURLConnection.HTTP_UNAUTHORIZED
                        )
                    }

                }

            } catch (throwable: Throwable) {
                throw throwable
            } finally {
                httpConnection.disconnect()
            }

        }.flowOn(IO)
}

private fun parseTracksList(connection: HttpURLConnection): List<TrackResponse> {
    val listType = object : TypeToken<List<TrackResponse>>() {}.type
    return gsonify().fromJson(connection.getStringResponse(), listType) as List<TrackResponse>
}

