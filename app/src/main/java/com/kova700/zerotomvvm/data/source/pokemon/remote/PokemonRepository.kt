package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.source.pokemon.Pokemon

interface PokemonRepository {

    suspend fun getPokemonList(
        size: Int = 20,
        page: Int = 0
    ): List<Pokemon>

}