package com.peter.music.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.peter.music.ApiStatus
import com.peter.music.BuildConfig.BASE_URL
import com.peter.music.Constant.ACCESSTOKEN
import com.peter.music.Constant.QUERY
import com.peter.music.Constant.SEARCH
import com.peter.music.HeadersKey
import com.peter.music.HeadersKey.AUTHORIZATION
import com.peter.music.HeadersValue
import com.peter.music.HeadersValue.BEARER
import com.peter.music.pojo.Track
import com.peter.music.service.Utilities.getStringResponse
import com.peter.music.service.Utilities.gsonify
import com.peter.music.service.Utilities.setHeaders
import java.net.HttpRetryException
import java.net.HttpURLConnection
import java.net.URL
import com.peter.music.service.responses.TrackResponse
import com.peter.music.service.parseToPlainObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SearchSource {
    suspend fun searchForTrack(
        query: String,
        liveData: MutableLiveData<List<Track>>,
        status: MutableLiveData<ApiStatus>
    )
}

class SearchSourceImpl @Inject constructor(
    private val token: TokenSource,
    private val auth: Auth
) : SearchSource {
    override suspend fun searchForTrack(
        query: String,
        liveData: MutableLiveData<List<Track>>,
        status: MutableLiveData<ApiStatus>
    ) {
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
                    val list = responseList.filter { it.title != null }
                        .map { it.parseToPlainObject() }
                    liveData.postValue(list)
                    if (list.isEmpty())
                        status.postValue(ApiStatus.EMPTY)
                    else status.postValue(ApiStatus.DONE)

                }
                HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    Log.i("Auth", "Not authenticated")
                    auth.authenticate()
                    liveData.postValue(ArrayList<Track>())
                }
                HttpURLConnection.HTTP_NOT_FOUND -> {
                    liveData.postValue(ArrayList<Track>())
                    status.postValue(ApiStatus.ERROR)
                }


            }

        } catch (throwable: Throwable) {
            throw throwable
        } finally {
            httpConnection.disconnect()
        }


    }

    private fun parseTracksList(connection: HttpURLConnection): List<TrackResponse> {
        val listType = object : TypeToken<List<TrackResponse>>() {}.type
        return gsonify().fromJson(connection.getStringResponse(), listType) as List<TrackResponse>
    }
}
