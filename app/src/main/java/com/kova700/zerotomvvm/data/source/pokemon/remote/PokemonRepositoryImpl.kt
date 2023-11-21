package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonDao
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import com.kova700.zerotomvvm.data.source.pokemon.toDBEntity
import com.kova700.zerotomvvm.data.source.pokemon.toListItem

class PokemonRepositoryImpl private constructor(
    private val pokemonService: PokemonApi,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override suspend fun loadRemotePokemonList(limit: Int, offset: Int): List<PokemonListItem> {
        val response = pokemonService.getPokemon(limit, offset)
        savePokemonListToLocalDB(response.results.toDBEntity())
        return loadLocalPokemonList(limit, offset)
    }

    override suspend fun loadLocalPokemonList(limit: Int, offset: Int): List<PokemonListItem> {
        return pokemonDao.getPokemonList(limit, offset).toListItem()
    }

    override suspend fun loadLocalWishPokemonList(limit: Int, offset: Int): List<PokemonListItem> {
        return pokemonDao.getPokemonListFromHeart(limit, offset, true).toListItem()
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