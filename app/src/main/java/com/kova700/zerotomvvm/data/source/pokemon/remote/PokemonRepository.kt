package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun loadRemotePokemonList(
        offset: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onSuccess: (List<PokemonListItem>) -> Unit,
        onFailure: (Throwable) -> Unit,
        onLastData: () -> Unit
    )

    suspend fun loadAllLocalPokemonListSmallerThan(
        targetNum: Int,
        onSuccess: (List<PokemonListItem>) -> Unit
    )

    fun loadWishPokemonList(): Flow<List<PokemonListItem>>

    suspend fun savePokemonListToLocalDB(pokemonList: List<PokemonEntity>)
    suspend fun savePokemonToLocalDB(pokemon: PokemonEntity)
    suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean)
}