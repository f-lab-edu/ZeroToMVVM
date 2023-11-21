package com.kova700.zerotomvvm.data.source.pokemon.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kova700.zerotomvvm.data.api.PokemonApi.Companion.GET_POKEMON_API_PAGING_SIZE

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonList(pokemonList: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Query(
        "SELECT * FROM PokemonEntity " +
                "WHERE num <= :targetNum "
    )
    suspend fun getAllPokemonListSmallerThan(targetNum: Int): List<PokemonEntity>

    @Query(
        "SELECT * FROM PokemonEntity " +
                "WHERE num >= :offset AND heart = :heartValue " +
                "LIMIT :limit "
    )
    suspend fun getPokemonListFromHeart(
        offset: Int,
        heartValue: Boolean,
        limit: Int = GET_POKEMON_API_PAGING_SIZE
    ): List<PokemonEntity>

    @Query(
        "Update PokemonEntity SET heart = :heartValue " +
                "WHERE num = :targetPokemonNum"
    )
    suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean)

}