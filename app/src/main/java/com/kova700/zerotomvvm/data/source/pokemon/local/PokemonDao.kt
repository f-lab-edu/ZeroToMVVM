package com.kova700.zerotomvvm.data.source.pokemon.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonList(pokemonList: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Query(
        "SELECT * FROM PokemonEntity " +
                "WHERE num >= :offset " +
                "LIMIT :limit "
    )
    suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): List<PokemonEntity>

    @Query(
        "Update PokemonEntity SET heart = :heartValue " +
                "WHERE num = :targetPokemonNum"
    )
    suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean)

}