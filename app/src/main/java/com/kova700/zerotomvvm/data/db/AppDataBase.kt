package com.kova700.zerotomvvm.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kova700.zerotomvvm.App
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonDao
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity

@Database(
    entities = [PokemonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao

    companion object {
        private const val DB_NAME = "ZeroToMVVM_DB"

        val service: AppDataBase by lazy { buildDataBase() }

        private fun buildDataBase(): AppDataBase {
            return Room.databaseBuilder(
                App.instance.applicationContext,
                AppDataBase::class.java,
                DB_NAME
            ).build()
        }
    }
}