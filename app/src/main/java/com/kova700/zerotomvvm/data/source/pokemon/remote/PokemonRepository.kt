package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity

interface PokemonRepository {

    suspend fun loadRemotePokemonList(
        limit: Int = 20,
        offset: Int = 0
    ): List<PokemonListItem>

    suspend fun loadLocalPokemonList(
        limit: Int = 20,
        offset: Int = 0
    ): List<PokemonListItem>

    suspend fun loadLocalWishPokemonList(
        limit: Int = 20,
        offset: Int = 0
    ): List<PokemonListItem>

    suspend fun savePokemonListToLocalDB(pokemonList: List<PokemonEntity>)
    suspend fun savePokemonToLocalDB(pokemon: PokemonEntity)
    suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean)
}