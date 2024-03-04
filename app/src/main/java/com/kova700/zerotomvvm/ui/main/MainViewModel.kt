package com.kova700.zerotomvvm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kova700.zerotomvvm.domain.repositry.PokemonRepository
import com.kova700.zerotomvvm.ui.main.MainUiState.UiState
import com.kova700.zerotomvvm.ui.main.mapper.toPokemon
import com.kova700.zerotomvvm.ui.main.mapper.toPokemonListItem
import com.kova700.zerotomvvm.ui.main.model.PokemonListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val pokemonRepository: PokemonRepository
) : ViewModel() {

	private val _eventFlow = MutableSharedFlow<PokemonUiEvent>()
	val eventFlow get() = _eventFlow.asSharedFlow()

	private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Default)
	val uiState get() = _uiState.asStateFlow()

	init {
		observePokemons()
		observeWishPokemons()
		getPokemons()
	}

	private fun observePokemons() = viewModelScope.launch {
		pokemonRepository.getPokemonsFlow().collect { newPokemons ->
			updateState { copy(pokemons = newPokemons.toPokemonListItem()) }
		}
	}

	private fun observeWishPokemons() = viewModelScope.launch {
		pokemonRepository.getWishPokemonsFlow().collect { wishedPokemons ->
			updateState { copy(wishPokemons = wishedPokemons.toPokemonListItem()) }
		}
	}

	private fun getPokemons() = viewModelScope.launch {
		updateState { copy(uiState = UiState.LOADING) }
		runCatching { pokemonRepository.getPokemons() }
			.onSuccess { updateState { copy(uiState = UiState.SUCCESS) } }
			.onFailure {
				ShowToast("서버로부터 데이터 load를 실패했습니다. : ${it.message}")
				updateState { copy(uiState = UiState.ERROR) }
			}
	}

	fun loadNextPokemons(lastVisibleItemPosition: Int) {
		if (uiState.value.pokemons.size - 1 > lastVisibleItemPosition ||
			uiState.value.uiState == UiState.LOADING
		) return
		getPokemons()
	}

	fun onRetryClick() {
		getPokemons()
	}

	fun onItemClick(itemPosition: Int) {
		startEvent(MoveToDetail(itemPosition))
	}

	fun onHeartClick(selectedItem: PokemonListItem) {
		updatePokemonHeart(selectedItem)
	}

	private fun updatePokemonHeart(selectedItem: PokemonListItem) = viewModelScope.launch {
		pokemonRepository.updatePokemonHeart(selectedItem.toPokemon(), selectedItem.heart.not())
	}

	private fun startEvent(event: PokemonUiEvent) = viewModelScope.launch {
		_eventFlow.emit(event)
	}

	private inline fun updateState(block: MainUiState.() -> MainUiState) {
		_uiState.update {
			_uiState.value.block()
		}
	}

}