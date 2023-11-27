package com.kova700.zerotomvvm.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kova700.zerotomvvm.data.api.PokemonApi.Companion.GET_POKEMON_API_PAGING_SIZE
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import com.kova700.zerotomvvm.data.source.pokemon.local.getRandomDummyEntity
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import com.kova700.zerotomvvm.util.Failure
import com.kova700.zerotomvvm.util.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//TODO : Local , Remote 데이터 구분은 Repository에서 하고, 구분 내용을 가져오지 않도록 수정
//TODO : 외부에서 Flow에 emit못하게 BackingProperty로 가시성 설정
class PokemonViewModel(private val pokemonRepository: PokemonRepository) : ViewModel() {

    val eventFlow = MutableSharedFlow<PokemonUiEvent>()
    var isLoading = MutableStateFlow(false)
    private var isLastDataLoaded = false

    private val pokemonNumOffset: MutableStateFlow<Int> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val pokemonListFlow = pokemonNumOffset
        .flatMapMerge { offset -> loadPokemonList(offset) }
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
        isLoading.value = true
        val result = pokemonRepository.loadPokemonList(offset)
        when (result) {
            is Success -> {
                isLastDataLoaded = result.isLast
            }

            is Failure -> {
                startEvent(ShowToast("서버로부터 데이터 load를 실패했습니다. : ${result.exception}"))
            }
        }
        isLoading.value = false
        return result.data
    }

    fun loadNextPokemonList(lastVisibleItemPosition: Int) {
        if (isLoading.value || isLastDataLoaded ||
            pokemonListFlow.value.size - 1 > lastVisibleItemPosition
        ) return
        pokemonNumOffset.value = pokemonNumOffset.value + GET_POKEMON_API_PAGING_SIZE
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
        eventFlow.emit(event)
    }

    sealed interface PokemonUiEvent
    data class MoveToDetail(val selectedItem: PokemonListItem) : PokemonUiEvent
    data class ShowToast(val message: String) : PokemonUiEvent

    companion object {
        fun provideFactory(pokemonRepository: PokemonRepository) =
            object : ViewModelProvider.Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PokemonViewModel(pokemonRepository) as T
                }
            }
    }

}