package com.kova700.zerotomvvm.view.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import com.kova700.zerotomvvm.view.main.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_EXTRA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var selectedItem: PokemonListItem =
        savedStateHandle.get<PokemonListItem>(TO_DETAIL_SELECTED_ITEM_EXTRA)!!

    private suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        pokemonRepository.updatePokemonHeart(targetPokemonNum, heartValue)
    }

    fun heartClickListener() = viewModelScope.launch {
        selectedItem = selectedItem.copy(heart = selectedItem.heart.not())
        updatePokemonHeart(selectedItem.pokemon.getPokemonNum(), selectedItem.heart)
    }

}