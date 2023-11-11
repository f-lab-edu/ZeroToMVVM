package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.source.pokemon.Pokemon

class PokemonRepositoryImpl(
    private val pokemonService: PokemonApi
) : PokemonRepository {

    override suspend fun getPokemonList(size: Int, page: Int): List<Pokemon> {
        val response = pokemonService.getPokemon(size, page)

        when (response.code()) {
            200 -> {
                val pokemonResponse = response.body()
                pokemonResponse?.let { return it.results }
                throw Exception("response body is empty")
            }

            else -> throw Exception("")
        }
    }
}