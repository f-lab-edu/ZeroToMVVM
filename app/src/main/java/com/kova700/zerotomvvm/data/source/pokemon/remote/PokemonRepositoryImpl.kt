package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.api.PokemonService
import com.kova700.zerotomvvm.data.api.PokemonService.Companion.GET_POKEMON_API_PAGING_SIZE
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
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokemonService: PokemonService,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    //이 상황에 맨아래 예외를 던지는게 맞는가..?
    override suspend fun loadPokemonList(offset: Int): Flow<NetworkResult<List<PokemonListItem>>> {
        runCatching { loadRemotePokemonList(offset) }
            .onSuccess { return it }
            .onFailure { throwable ->
                return loadAllLocalPokemonListSmallerThan(offset + GET_POKEMON_API_PAGING_SIZE)
                    .map {
                        Failure(
                            data = it,
                            exception = throwable
                        )
                    }
            }
        throw Exception() //뭔가 좀 애매하네..
    }

    private suspend fun loadRemotePokemonList(offset: Int): Flow<NetworkResult<List<PokemonListItem>>> {
        val response = pokemonService.getPokemon(offset = offset)
        val pokemonList = response.results
        insertPokemonList(pokemonList.toDBEntity())
        return loadAllLocalPokemonListSmallerThan(offset + GET_POKEMON_API_PAGING_SIZE)
            .map {
                Success(
                    data = it,
                    isLast = response.next == null
                )
            }
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
}