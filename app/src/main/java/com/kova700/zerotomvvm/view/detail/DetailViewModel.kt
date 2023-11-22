package com.kova700.zerotomvvm.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepositoryImpl
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val pokemonRepository: PokemonRepository by lazy {
        PokemonRepositoryImpl.getInstance(
            PokemonApi.service,
            AppDataBase.service
        )
    }

    private suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        pokemonRepository.updatePokemonHeart(targetPokemonNum, heartValue)
    }

    fun heartClickListener(selectedItem: PokemonListItem) = viewModelScope.launch {
        updatePokemonHeart(selectedItem.pokemon.getPokemonNum(), selectedItem.heart.not())
    }
}