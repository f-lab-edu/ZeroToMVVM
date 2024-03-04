package com.kova700.zerotomvvm.view.main

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

data class MainUiState(
	val pokemons: List<PokemonListItem>,
	val wishPokemons :List<PokemonListItem>,
	val uiState: UiState,
) {


	enum class UiState {
		SUCCESS,
		LOADING,
		ERROR,
		EMPTY,
	}

	companion object {
		val Default = MainUiState(
			pokemons = emptyList(),
			wishPokemons = emptyList(),
			uiState = UiState.EMPTY
		)
	}
}

sealed interface PokemonUiEvent
data class MoveToDetail(val selectedItem: PokemonListItem) : PokemonUiEvent
data class ShowToast(val message: String) : PokemonUiEvent