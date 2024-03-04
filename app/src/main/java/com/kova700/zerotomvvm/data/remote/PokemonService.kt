package com.kova700.zerotomvvm.data.remote

import com.kova700.zerotomvvm.data.remote.model.GetPokemonsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon")
    suspend fun getPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = GET_POKEMON_API_PAGING_SIZE
    ): GetPokemonsResponse

    companion object {
        const val GET_POKEMON_API_PAGING_SIZE = 30
    }
}