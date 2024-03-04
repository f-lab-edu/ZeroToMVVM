package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.api.PokemonService
import com.kova700.zerotomvvm.data.api.PokemonService.Companion.GET_POKEMON_API_PAGING_SIZE
import com.kova700.zerotomvvm.data.source.pokemon.Pokemon
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonDao
import com.kova700.zerotomvvm.data.source.pokemon.toListItem
import com.kova700.zerotomvvm.data.source.pokemon.toPokemonListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
	private val pokemonService: PokemonService,
	private val pokemonDao: PokemonDao
) : PokemonRepository {

	private val pokemons = MutableStateFlow<List<PokemonListItem>>(emptyList())
	private val wishedPokemons = pokemonDao.getWishPokemons(true).map { it.toListItem() }
	private val isWished = wishedPokemons.map { wishPokemons ->
		wishPokemons.associateBy { it.pokemon.getPokemonNum() } //(pokemonNum, pokemonEntity)
	}
	private val pokemonsWithWish = pokemons.combine(isWished) { pokemons, isWished ->
		pokemons.map { if (isWished[it.pokemon.getPokemonNum()] != null) it.copy(heart = true) else it }
	}

	private var currentPage: Int = 0
	private var isEndPage = false

	override fun getPokemonsFlow(): Flow<List<PokemonListItem>> {
		return pokemonsWithWish
	}

	override fun getWishPokemonsFlow(): Flow<List<PokemonListItem>> {
		return  wishedPokemons
	}

	override suspend fun getPokemons() {
		if (isEndPage) return

		val response = pokemonService.getPokemon(offset = currentPage)
		isEndPage = response.next == null
		currentPage += GET_POKEMON_API_PAGING_SIZE
		val newPokemons = response.results.map { it.toPokemonListItem() }
		pokemons.update { pokemons.value + newPokemons }
	}

	override suspend fun updatePokemonHeart(targetPokemon: Pokemon, desiredValue: Boolean) {
		if (desiredValue) {
			//heartValue가 true 라면 없는 상태에서 DB에 등록이 필요
			pokemonDao.insertPokemon(targetPokemon.toDBEntity())
			return
		}
		//heartValue가 false 라면 없는 상태에서 DB에서 제거가 필요
		pokemonDao.deletePokemon(targetPokemon.getPokemonNum())
	}

}