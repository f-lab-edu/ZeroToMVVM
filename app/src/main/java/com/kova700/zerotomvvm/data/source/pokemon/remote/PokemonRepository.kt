package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.source.pokemon.Pokemon
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import com.kova700.zerotomvvm.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
	suspend fun updatePokemonHeart(targetPokemon: Pokemon, desiredValue: Boolean)
	fun getPokemonsFlow(): Flow<List<PokemonListItem>>
	fun getWishPokemonsFlow(): Flow<List<PokemonListItem>>
	suspend fun getPokemons()
}