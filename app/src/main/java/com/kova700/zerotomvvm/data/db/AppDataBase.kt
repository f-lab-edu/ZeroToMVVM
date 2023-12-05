package com.kova700.zerotomvvm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonDao
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity

@Database(
    entities = [PokemonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}