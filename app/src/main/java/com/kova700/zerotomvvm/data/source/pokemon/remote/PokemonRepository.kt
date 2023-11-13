package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface PokemonRepository {

    val pokemonListFlow: MutableStateFlow<List<PokemonListItem>>
    val wishPokemonListFlow: Flow<List<PokemonListItem>>

    suspend fun loadPokemonList(
        size: Int = 20,
        page: Int = 0
    )

}