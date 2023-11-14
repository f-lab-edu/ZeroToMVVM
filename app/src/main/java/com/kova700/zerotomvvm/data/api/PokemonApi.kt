package com.kova700.zerotomvvm.data.api

import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemon(
        @Query("limit") size: Int,
        @Query("offset") offset: Int,
    ): PokemonResponse

    companion object {
        val service: PokemonApi by lazy { RetrofitBuilder.retrofit.create(PokemonApi::class.java) }
    }
}