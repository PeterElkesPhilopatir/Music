package com.peter.music.di

import com.peter.music.pojo.Track
import com.peter.music.service.*
import com.peter.music.service.repository.TracksRepo
import com.peter.music.service.repository.TracksRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TrackSearchProviderModule {

    @Singleton
    @Provides
    fun provideAuthenticator(token: TokenSource): Auth =
        AuthImpl(token)

    @Singleton
    @Provides
    fun providerSearchSource(
        token: TokenSource,
        auth: Auth
    ): SearchSource =
        SearchSourceImpl(token, auth)

    @Singleton
    @Provides
    fun providerTracksRepo(searchSource: SearchSource): TracksRepo =
        TracksRepoImpl(searchSource)

    @Singleton
    @Provides
    fun providerTracksUseCase(tracksRepo: TracksRepo): TracksUseCase =
        TracksUseCaseImpl(tracksRepo)

//    @Module
//    @InstallIn(ViewModelComponent::class)
//    internal object ViewModelMovieModule {
//        @Provides
//        @ViewModelScoped
//        fun providecase(tracksRepo: TracksRepo): TracksUseCase =
//            TracksUseCaseImpl(tracksRepo);
//    }
}