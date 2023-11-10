package com.kova700.zerotomvvm

interface PokemonRepository {

    suspend fun getPokemonList(
        size: Int = 20,
        page: Int = 0
    ): List<Pokemon>

}