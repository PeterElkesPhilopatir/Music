package com.peter.music.service.repository

import androidx.lifecycle.MutableLiveData
import com.peter.music.ApiStatus
import com.peter.music.pojo.Track
import com.peter.music.service.SearchSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TracksRepo {
    suspend fun getTracks(query: String, liveData: MutableLiveData<List<Track>>,status : MutableLiveData<ApiStatus>)
}

class TracksRepoImpl @Inject constructor(private val search: SearchSource) : TracksRepo {
    override suspend fun getTracks(query: String, liveData: MutableLiveData<List<Track>>,status : MutableLiveData<ApiStatus>) =
        search.searchForTrack(query, liveData,status)
}