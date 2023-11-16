package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.source.pokemon.Pokemon
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

class PokemonRepositoryImpl private constructor(
    private val pokemonService: PokemonApi
) : PokemonRepository {

    override var pokemonList: List<PokemonListItem> = listOf()
    override val wishPokemonList: List<PokemonListItem>
        get() = pokemonList.filter { it.heart }

    override suspend fun loadPokemonList(size: Int, page: Int): List<PokemonListItem> {
        if (pokemonList.isNotEmpty()) return pokemonList

        val response = pokemonService.getPokemon(size, page)
        pokemonList = response.results
            .map { pokemon: Pokemon -> PokemonListItem(pokemon) }

        return pokemonList
    }

    companion object {
        @Volatile
        private var INSTANCE: PokemonRepository? = null

        fun getInstance(pokemonService: PokemonApi): PokemonRepository =
            INSTANCE ?: synchronized(this) {
                PokemonRepositoryImpl(pokemonService).apply { INSTANCE = this }
            }
    }
}