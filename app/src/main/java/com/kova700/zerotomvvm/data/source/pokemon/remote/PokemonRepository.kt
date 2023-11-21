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
    //다른 load는 반환값이 있는데,
    //이건 없고 통일이 필요해 보임

    suspend fun loadLocalWishPokemonList(
        offset: Int = 0
    ): List<PokemonListItem>

    suspend fun loadAllLocalPokemonListSmallerThan(
        targetNum: Int
    ): List<PokemonListItem>

    suspend fun savePokemonListToLocalDB(pokemonList: List<PokemonEntity>)
    suspend fun savePokemonToLocalDB(pokemon: PokemonEntity)
    suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean)
}