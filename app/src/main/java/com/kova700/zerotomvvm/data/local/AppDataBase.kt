package com.kova700.zerotomvvm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kova700.zerotomvvm.data.local.model.PokemonEntity

@Database(
    entities = [PokemonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}