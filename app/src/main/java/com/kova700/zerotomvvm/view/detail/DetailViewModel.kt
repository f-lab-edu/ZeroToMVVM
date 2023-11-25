package com.kova700.zerotomvvm.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val pokemonRepository: PokemonRepository,
    var selectedItem: PokemonListItem
) : ViewModel() {

    private suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        pokemonRepository.updatePokemonHeart(targetPokemonNum, heartValue)
    }

    fun heartClickListener() = viewModelScope.launch {
        selectedItem = selectedItem.copy(heart = selectedItem.heart.not())
        updatePokemonHeart(selectedItem.pokemon.getPokemonNum(), selectedItem.heart)
    }

    companion object {
        fun provideFactory(
            pokemonRepository: PokemonRepository,
            selectedItem: PokemonListItem
        ) = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailViewModel(pokemonRepository, selectedItem) as T
            }
        }
    }
}