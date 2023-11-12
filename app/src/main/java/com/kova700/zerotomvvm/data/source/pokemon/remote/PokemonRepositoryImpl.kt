package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.source.pokemon.Pokemon
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class PokemonRepositoryImpl(
    private val pokemonService: PokemonApi
) : PokemonRepository {

    override val pokemonListFlow: MutableStateFlow<List<PokemonListItem>> =
        MutableStateFlow(listOf())
    override val wishPokemonListFlow: Flow<List<PokemonListItem>>
        get() = pokemonListFlow.map { list -> list.filter { it.heart } }

    override suspend fun loadPokemonList(size: Int, page: Int) {
        val response = pokemonService.getPokemon(size, page)

        when (response.code()) {
            200 -> {
                val pokemonResponse = response.body()
                pokemonResponse?.let {
                    pokemonListFlow.emit(
                        it.results.map { pokemon: Pokemon ->
                            PokemonListItem(pokemon)
                        }
                    )
                }
            }

            else -> throw Exception("")
        }
    }
}