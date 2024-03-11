package com.kova700.zerotomvvm.di

import com.kova700.zerotomvvm.data.remote.PokemonService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun providePokemonService(retrofit: Retrofit): PokemonService =
        retrofit.create(PokemonService::class.java)
}