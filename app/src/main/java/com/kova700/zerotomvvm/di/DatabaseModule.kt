package com.kova700.zerotomvvm.di

import android.app.Application
import androidx.room.Room
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application
    ): AppDataBase = Room
        .databaseBuilder(application, AppDataBase::class.java, AppDataBase.DB_NAME)
        .build()

    @Provides
    @Singleton
    fun providePokemonDao(appDatabase: AppDataBase): PokemonDao {
        return appDatabase.pokemonDao()
    }
}