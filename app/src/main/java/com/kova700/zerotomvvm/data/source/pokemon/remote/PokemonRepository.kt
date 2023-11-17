package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

interface PokemonRepository {

    var pokemonList: List<PokemonListItem>
    val wishPokemonList: List<PokemonListItem>
        get() = pokemonList.filter { it.heart }

    suspend fun loadRemotePokemonList(
        size: Int = 20,
        page: Int = 0
    ): List<PokemonListItem>

    suspend fun loadLocalPokemonList(
        size: Int = 20,
        page: Int = 0
    ): List<PokemonListItem>

}