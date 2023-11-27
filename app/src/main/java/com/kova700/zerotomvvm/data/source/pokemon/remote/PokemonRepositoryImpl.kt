package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.api.PokemonApi.Companion.GET_POKEMON_API_PAGING_SIZE
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonDao
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import com.kova700.zerotomvvm.data.source.pokemon.toDBEntity
import com.kova700.zerotomvvm.data.source.pokemon.toListItem
import com.kova700.zerotomvvm.util.Failure
import com.kova700.zerotomvvm.util.NetworkResult
import com.kova700.zerotomvvm.util.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonRepositoryImpl private constructor(
    private val pokemonService: PokemonApi,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override suspend fun loadPokemonList(offset: Int): NetworkResult<Flow<List<PokemonListItem>>> {
        runCatching { loadRemotePokemonList(offset) }
            .onSuccess { isLast ->
                return Success(
                    data = loadAllLocalPokemonListSmallerThan(offset + GET_POKEMON_API_PAGING_SIZE),
                    isLast = isLast
                )
            }
            .onFailure {
                return Failure(
                    data = loadAllLocalPokemonListSmallerThan(offset + GET_POKEMON_API_PAGING_SIZE),
                    exception = it
                )
            }
        throw Exception() //뭔가 좀 애매하네..
    }

    private suspend fun loadRemotePokemonList(offset: Int): Boolean {
        val response = pokemonService.getPokemon(offset = offset)
        val pokemonList = response.results
        insertPokemonList(pokemonList.toDBEntity())
        val isLast = response.next == null
        return isLast
    }

    private fun loadAllLocalPokemonListSmallerThan(targetNum: Int): Flow<List<PokemonListItem>> {
        return pokemonDao.getAllPokemonListSmallerThan(targetNum).map { it.toListItem() }
    }

    override fun loadWishPokemonList(): Flow<List<PokemonListItem>> {
        return pokemonDao.getPokemonListFromHeart(true).map { it.toListItem() }
    }

    override suspend fun insertPokemonList(pokemonList: List<PokemonEntity>) {
        pokemonDao.insertPokemonList(pokemonList)
    }

    override suspend fun insertPokemonItem(pokemon: PokemonEntity) {
        pokemonDao.insertPokemon(pokemon)
    }

    override suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        pokemonDao.updatePokemonHeart(targetPokemonNum, heartValue)
    }

    companion object {
        @Volatile
        private var INSTANCE: PokemonRepository? = null

        fun getInstance(pokemonService: PokemonApi, appDatabase: AppDataBase): PokemonRepository =
            INSTANCE ?: synchronized(this) {
                PokemonRepositoryImpl(
                    pokemonService,
                    appDatabase.pokemonDao()
                ).apply { INSTANCE = this }
            }
    }
}