package com.kova700.zerotomvvm.di

import com.kova700.zerotomvvm.domain.repositry.PokemonRepository
import com.kova700.zerotomvvm.data.repository.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindPokemonRepository(repository: PokemonRepositoryImpl): PokemonRepository
}