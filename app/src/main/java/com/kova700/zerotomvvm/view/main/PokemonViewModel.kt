package com.kova700.zerotomvvm.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kova700.zerotomvvm.data.api.PokemonService.Companion.GET_POKEMON_API_PAGING_SIZE
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import com.kova700.zerotomvvm.data.source.pokemon.local.getRandomDummyEntity
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import com.kova700.zerotomvvm.util.Failure
import com.kova700.zerotomvvm.util.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<PokemonUiEvent>()
    val eventFlow: SharedFlow<PokemonUiEvent>
        get() = _eventFlow

    private var _isLoading = MutableStateFlow(false)
    val isLoading: SharedFlow<Boolean>
        get() = _isLoading

    private var isLastDataLoaded = false
    private val pokemonNumOffset: MutableStateFlow<Int> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val pokemonListFlow = pokemonNumOffset
        .flatMapLatest { offset -> loadPokemonList(offset) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = listOf()
        )

    val wishPokemonListFlow = pokemonRepository.loadWishPokemonList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = listOf()
        )

    private suspend fun insertPokemonItem(pokemonEntity: PokemonEntity) {
        pokemonRepository.insertPokemonItem(pokemonEntity)
    }

    private suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        pokemonRepository.updatePokemonHeart(targetPokemonNum, heartValue)
    }

    fun plusBtnClickListener() = viewModelScope.launch {
        insertPokemonItem(
            getRandomDummyEntity(pokemonListFlow.value.size + 1)
        )
    }

    private suspend fun loadPokemonList(offset: Int = 0): Flow<List<PokemonListItem>> {
        _isLoading.update { true }
        val result = pokemonRepository.loadPokemonList(offset)
        when (result) {
            is Success -> {
                isLastDataLoaded = result.isLast
            }

            is Failure -> {
                startEvent(ShowToast("서버로부터 데이터 load를 실패했습니다. : ${result.exception}"))
            }
        }
        _isLoading.update { false }
        return result.data
    }

    fun loadNextPokemonList(lastVisibleItemPosition: Int) {
        if (_isLoading.value || isLastDataLoaded ||
            pokemonListFlow.value.size - 1 > lastVisibleItemPosition
        ) return

        _isLoading.update { true }
        pokemonNumOffset.update { pokemonNumOffset.value + GET_POKEMON_API_PAGING_SIZE }
    }

    fun itemClickListener(selectedItem: PokemonListItem) = viewModelScope.launch {
        startEvent(MoveToDetail(selectedItem))
    }

    fun wishHeartClickListener(selectedItem: PokemonListItem) = viewModelScope.launch {
        if (selectedItem.heart.not()) return@launch
        updatePokemonHeart(selectedItem.pokemon.getPokemonNum(), false)
    }

    fun homeHeartClickListener(selectedItem: PokemonListItem) = viewModelScope.launch {
        updatePokemonHeart(
            selectedItem.pokemon.getPokemonNum(),
            selectedItem.heart.not()
        )
    }

    private suspend fun startEvent(event: PokemonUiEvent) {
        _eventFlow.emit(event)
    }

    sealed interface PokemonUiEvent
    data class MoveToDetail(val selectedItem: PokemonListItem) : PokemonUiEvent
    data class ShowToast(val message: String) : PokemonUiEvent
}