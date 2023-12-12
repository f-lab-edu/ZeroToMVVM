package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import com.kova700.zerotomvvm.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun loadPokemonList(
        offset: Int
    ): Flow<NetworkResult<List<PokemonListItem>>>

    fun loadWishPokemonList(): Flow<List<PokemonListItem>>

    suspend fun insertPokemonList(pokemonList: List<PokemonEntity>)
    suspend fun insertPokemonItem(pokemon: PokemonEntity)
    suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean)
}