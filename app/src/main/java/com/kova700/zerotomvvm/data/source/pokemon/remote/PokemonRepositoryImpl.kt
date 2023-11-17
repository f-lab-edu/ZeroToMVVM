package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonDao
import com.kova700.zerotomvvm.data.source.pokemon.toDBEntity
import com.kova700.zerotomvvm.data.source.pokemon.toListItem

class PokemonRepositoryImpl private constructor(
    private val pokemonService: PokemonApi,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override var pokemonList: List<PokemonListItem> = listOf()
    override val wishPokemonList: List<PokemonListItem>
        get() = pokemonList.filter { it.heart }

    override suspend fun loadRemotePokemonList(size: Int, page: Int): List<PokemonListItem> {
        val response = pokemonService.getPokemon(size, page)
        pokemonDao.insertPokemonList(response.results.toDBEntity())
        return loadLocalPokemonList(size, page)
    }

    override suspend fun loadLocalPokemonList(size: Int, page: Int): List<PokemonListItem> {
        pokemonList = pokemonDao.getPokemonList(page).toListItem()
        return pokemonList
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