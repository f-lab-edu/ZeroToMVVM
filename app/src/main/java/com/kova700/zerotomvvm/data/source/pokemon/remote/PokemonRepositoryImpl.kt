package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.api.PokemonApi.Companion.GET_POKEMON_API_PAGING_SIZE
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonDao
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import com.kova700.zerotomvvm.data.source.pokemon.toDBEntity
import com.kova700.zerotomvvm.data.source.pokemon.toListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonRepositoryImpl private constructor(
    private val pokemonService: PokemonApi,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override suspend fun loadRemotePokemonList(
        offset: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onSuccess: (List<PokemonListItem>) -> Unit,
        onFailure: (Throwable) -> Unit,
        onLastData: () -> Unit,
    ) {
        onStart()
        runCatching { pokemonService.getPokemon(offset = offset) }
            .onSuccess {
                if (it.next.isNullOrBlank()) onLastData()
                savePokemonListToLocalDB(it.results.toDBEntity())
                loadAllLocalPokemonListSmallerThan(
                    targetNum = offset + GET_POKEMON_API_PAGING_SIZE,
                    onSuccess = onSuccess,
                )
                onComplete()
            }
            .onFailure { onFailure(it) }
    }

    override suspend fun loadAllLocalPokemonListSmallerThan(
        targetNum: Int,
        onSuccess: (List<PokemonListItem>) -> Unit
    ) {
        runCatching { pokemonDao.getAllPokemonListSmallerThan(targetNum).toListItem() }
            .onSuccess { onSuccess(it) }
    }

    override fun loadWishPokemonList(): Flow<List<PokemonListItem>> {
        return pokemonDao.getPokemonListFromHeart(true).map { it.toListItem() }
    }

    override suspend fun savePokemonListToLocalDB(pokemonList: List<PokemonEntity>) {
        pokemonDao.insertPokemonList(pokemonList)
    }

    override suspend fun savePokemonToLocalDB(pokemon: PokemonEntity) {
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