package com.kova700.zerotomvvm

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemon(
        @Query("limit") size: Int,
        @Query("offset") offset: Int,
    ): Response<PokemonResponse>

    companion object {
        val service: PokemonApi by lazy { RetrofitBuilder.retrofit.create(PokemonApi::class.java) }
    }
}