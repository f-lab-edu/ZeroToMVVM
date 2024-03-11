package com.kova700.zerotomvvm.data.repository

import com.kova700.zerotomvvm.data.local.PokemonDao
import com.kova700.zerotomvvm.data.mapper.toPokemonEntity
import com.kova700.zerotomvvm.data.mapper.toPokemonFromEntity
import com.kova700.zerotomvvm.data.mapper.toPokemonFromResponse
import com.kova700.zerotomvvm.data.remote.PokemonService
import com.kova700.zerotomvvm.data.remote.PokemonService.Companion.GET_POKEMON_API_PAGING_SIZE
import com.kova700.zerotomvvm.domain.model.Pokemon
import com.kova700.zerotomvvm.domain.repositry.PokemonRepository
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

	private val pokemons = MutableStateFlow<List<Pokemon>>(emptyList())

	private val wishedPokemons = pokemonDao.getWishPokemons(true).map { it.toPokemonFromEntity() }
		.onEach { cachedWishPokemons = it }
	private val isWished = wishedPokemons.map { wishPokemons ->
		wishPokemons.associateBy { it.num } //(pokemonNum, pokemonEntity)
	}
	private val pokemonsWithWish = pokemons.combine(isWished) { pokemons, isWished ->
		pokemons.map { if (isWished[it.num] != null) it.copy(heart = true) else it }
	}.onEach { cachedPokemonsWithWish = it }

	private var cachedPokemonsWithWish: List<Pokemon> = emptyList()
	private var cachedWishPokemons: List<Pokemon> = emptyList()

	private var currentPage: Int = 0
	private var isEndPage = false

	override fun getPokemonsFlow(): Flow<List<Pokemon>> {
		return pokemonsWithWish
	}

	override fun getWishPokemonsFlow(): Flow<List<Pokemon>> {
		return wishedPokemons
	}

	override suspend fun getPokemons() {
		if (isEndPage) return

		val response = pokemonService.getPokemons(offset = currentPage)

		isEndPage = response.next == null
		currentPage += GET_POKEMON_API_PAGING_SIZE

		val newPokemons = response.results.toPokemonFromResponse()
		pokemons.update { pokemons.value + newPokemons }
	}

	override fun getCachedPokemonFromIndex(index: Int): Pokemon {
		return cachedPokemonsWithWish[index]
	}

	override fun getCachedWishPokemonFromIndex(index: Int): Pokemon {
		return cachedWishPokemons[index]
	}

	override suspend fun updatePokemonHeart(targetPokemon: Pokemon, desiredValue: Boolean) {
		if (desiredValue) {
			//heartValue가 true 라면 없는 상태에서 DB에 등록이 필요
			pokemonDao.insertPokemon(targetPokemon.toPokemonEntity())
			return
		}
		//heartValue가 false 라면 없는 상태에서 DB에서 제거가 필요
		pokemonDao.deletePokemon(targetPokemon.num)
	}

}