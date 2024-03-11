package com.kova700.zerotomvvm.domain.repositry

import com.kova700.zerotomvvm.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
	suspend fun updatePokemonHeart(targetPokemon: Pokemon, desiredValue: Boolean)
	fun getPokemonsFlow(): Flow<List<Pokemon>>
	fun getWishPokemonsFlow(): Flow<List<Pokemon>>
	suspend fun getPokemons()
	fun getCachedPokemonFromIndex(index: Int): Pokemon
	fun getCachedWishPokemonFromIndex(index: Int): Pokemon
}