package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity

interface PokemonRepository {
    var lastLoadPokemonNum: Int

    suspend fun loadRemotePokemonList(
        offset: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onSuccess: (List<PokemonListItem>) -> Unit,
        onFailure: (Throwable) -> Unit,
        onLastData: () -> Unit,
    )

    suspend fun loadLocalWishPokemonList(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onSuccess: (List<PokemonListItem>) -> Unit,
        onFailure: (Throwable) -> Unit,
    )

    suspend fun loadAllLocalPokemonListSmallerThan(
        targetNum: Int,
        onSuccess: (List<PokemonListItem>) -> Unit
    )

    suspend fun savePokemonListToLocalDB(pokemonList: List<PokemonEntity>)
    suspend fun savePokemonToLocalDB(pokemon: PokemonEntity)
    suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean)
}