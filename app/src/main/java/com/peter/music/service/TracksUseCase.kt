package com.peter.music.service

import androidx.lifecycle.MutableLiveData
import com.peter.music.ApiStatus
import com.peter.music.pojo.Track
import com.peter.music.service.repository.TracksRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TracksUseCase {
    suspend fun getTracks(query: String, liveData: MutableLiveData<List<Track>>,status : MutableLiveData<ApiStatus>)
}

class TracksUseCaseImpl @Inject constructor(private val repo: TracksRepo) :
    TracksUseCase {
    override suspend fun getTracks(query: String, liveData: MutableLiveData<List<Track>>,status : MutableLiveData<ApiStatus>) =
        repo.getTracks(query, liveData,status)
}