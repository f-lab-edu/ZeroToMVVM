package com.kova700.zerotomvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kova700.zerotomvvm.domain.model.Pokemon
import com.kova700.zerotomvvm.domain.repositry.PokemonRepository
import com.kova700.zerotomvvm.ui.main.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_INDEX_EXTRA
import com.kova700.zerotomvvm.ui.main.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_STATE_EXTRA
import com.kova700.zerotomvvm.ui.main.model.SelectedItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
	private val pokemonRepository: PokemonRepository,
	private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private val selectedItemIndex: Int =
		savedStateHandle.get<Int>(TO_DETAIL_SELECTED_ITEM_INDEX_EXTRA)!!
	private val selectedItemState: SelectedItemState =
		savedStateHandle.get<SelectedItemState>(TO_DETAIL_SELECTED_ITEM_STATE_EXTRA)!!

	private val _uiState = MutableStateFlow<Pokemon>(getPokemon())
	val uiState get() = _uiState.asStateFlow()

	private fun updatePokemonHeart() = viewModelScope.launch {
		pokemonRepository.updatePokemonHeart(uiState.value, uiState.value.heart.not())
		_uiState.update { it.copy(heart = it.heart.not()) }
	}

	private fun getPokemon(): Pokemon {
		return when (selectedItemState) {
			SelectedItemState.DEFAULT -> {
				pokemonRepository.getCachedPokemonFromIndex(selectedItemIndex)
			}

			SelectedItemState.WISH -> {
				pokemonRepository.getCachedWishPokemonFromIndex(selectedItemIndex)
			}
		}
	}

	fun onHeartClick() {
		updatePokemonHeart()
	}

}