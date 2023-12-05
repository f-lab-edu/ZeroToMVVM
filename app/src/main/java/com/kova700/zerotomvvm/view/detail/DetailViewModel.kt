package com.kova700.zerotomvvm.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class DetailViewModel @AssistedInject constructor(
    private val pokemonRepository: PokemonRepository,
    @Assisted var selectedItem: PokemonListItem
) : ViewModel() {

    private suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        pokemonRepository.updatePokemonHeart(targetPokemonNum, heartValue)
    }

    fun heartClickListener() = viewModelScope.launch {
        selectedItem = selectedItem.copy(heart = selectedItem.heart.not())
        updatePokemonHeart(selectedItem.pokemon.getPokemonNum(), selectedItem.heart)
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(selectedItem: PokemonListItem): DetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            selectedItem: PokemonListItem
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(selectedItem) as T
            }
        }
    }
}