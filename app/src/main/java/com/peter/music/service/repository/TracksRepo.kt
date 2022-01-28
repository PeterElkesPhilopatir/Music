package com.peter.music.service.repository

import com.peter.music.pojo.Track
import com.peter.music.service.SearchSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TracksRepo {
    suspend fun getTracks(query: String): Flow<List<Track>>
}
class TracksRepoImpl @Inject constructor(private val search: SearchSource)  : TracksRepo {
    override suspend fun getTracks(query: String) =
        search.searchForTrack(query)
}