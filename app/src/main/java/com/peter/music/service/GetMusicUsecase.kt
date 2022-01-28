package com.peter.music.service

import com.peter.music.pojo.Track
import com.peter.music.service.repository.TracksRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TracksUseCase {
    suspend fun getTracks(query: String): Flow<List<Track>>
}
class TracksUseCaseImpl @Inject constructor(private val repo: TracksRepo) :
    TracksUseCase {
    override suspend fun getTracks(query: String) = repo.getTracks(query)
}