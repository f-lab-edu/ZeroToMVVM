package com.kova700.zerotomvvm.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepositoryImpl
import com.kova700.zerotomvvm.view.main.PokemonViewModel
import kotlinx.coroutines.launch

class DetailViewModel(private val pokemonRepository: PokemonRepository) : ViewModel() {

    private suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        pokemonRepository.updatePokemonHeart(targetPokemonNum, heartValue)
    }

    fun heartClickListener(selectedItem: PokemonListItem) = viewModelScope.launch {
        updatePokemonHeart(selectedItem.pokemon.getPokemonNum(), selectedItem.heart.not())
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailViewModel(
                    PokemonRepositoryImpl.getInstance(
                        PokemonApi.service,
                        AppDataBase.service
                    )
                ) as T
            }
        }
    }
}