package com.peter.music.di

import com.peter.music.service.Auth
import com.peter.music.service.TokenSource
import com.peter.music.service.TokenSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenProviderModule {

    @Binds
    abstract fun bindPrefsStore(tokenStore: TokenSourceImpl): TokenSource

}