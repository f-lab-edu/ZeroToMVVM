package com.kova700.zerotomvvm.data.source.pokemon.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonList: List<PokemonEntity>)

    @Query(
        "SELECT * FROM PokemonEntity " +
                "WHERE num >= :startNum " +
                "LIMIT :limit "
    )
    suspend fun getPokemonList(
        startNum: Int,
        limit: Int = 20
    ): List<PokemonEntity>

    @Query(
        "Update PokemonEntity SET heart = :heartValue " +
                "WHERE num = :targetPokemonNum"
    )
    suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean)

}