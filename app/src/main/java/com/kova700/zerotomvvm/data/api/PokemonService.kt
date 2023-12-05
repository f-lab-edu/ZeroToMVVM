package com.kova700.zerotomvvm.data.api

import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon")
    suspend fun getPokemon(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = GET_POKEMON_API_PAGING_SIZE
    ): PokemonResponse

    companion object {
        const val GET_POKEMON_API_PAGING_SIZE = 30
    }
}