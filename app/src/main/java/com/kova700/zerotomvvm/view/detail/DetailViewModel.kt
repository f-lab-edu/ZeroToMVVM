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

	private fun updatePokemonHeart() = viewModelScope.launch {
		pokemonRepository.updatePokemonHeart(selectedItem.pokemon, selectedItem.heart.not())
		selectedItem = selectedItem.copy(heart = selectedItem.heart.not())
	}

	fun onHeartClick(){
		updatePokemonHeart()
	}

}