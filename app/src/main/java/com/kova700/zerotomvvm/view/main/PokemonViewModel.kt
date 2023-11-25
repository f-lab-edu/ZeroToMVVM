package com.kova700.zerotomvvm.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kova700.zerotomvvm.data.api.PokemonApi.Companion.GET_POKEMON_API_PAGING_SIZE
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import com.kova700.zerotomvvm.data.source.pokemon.local.getRandomDummyEntity
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//TODO : 로컬 DB혹은 Repository에서 Flow로 받아오게 수정해보자.
class PokemonViewModel(private val pokemonRepository: PokemonRepository) : ViewModel() {

    private var isPokemonLastData: Boolean = false
    private var lastLoadPokemonNum = 0

    val eventFlow = MutableSharedFlow<PokemonUiEvent>()
    val pokemonListFlow = MutableStateFlow<List<PokemonListItem>>(listOf())
    val wishPokemonListFlow = pokemonRepository.loadWishPokemonList().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )
    var isLoading = MutableStateFlow(false)

    init {
        viewModelScope.launch { loadRemotePokemonList() }
    }

    private suspend fun loadRemotePokemonList(offset: Int = 0) {
        pokemonRepository.loadRemotePokemonList(
            offset = offset,
            onStart = { isLoading.value = true },
            onComplete = { isLoading.value = false },
            onSuccess = {
                pokemonListFlow.value = it
                lastLoadPokemonNum = it.last().pokemon.getPokemonNum()
            },
            onFailure = {
                loadRemotePokemonListFailCallback(it)
            },
            onLastData = { isPokemonLastData = true }
        )
    }

    //서버로부터 API요청이 실패하면 이전에 로컬 DB에 저장해놨던 데이터를 가져옴
    private fun loadRemotePokemonListFailCallback(throwable: Throwable) = viewModelScope.launch {
        startEvent(ShowToast("서버로부터 데이터 load를 실패했습니다. : ${throwable.message}"))
        loadAllLocalPokemonListSmallerThan(
            lastLoadPokemonNum + GET_POKEMON_API_PAGING_SIZE
        )
        isLoading.value = false
    }

    private suspend fun loadAllLocalPokemonListSmallerThan(targetNum: Int) {
        pokemonRepository.loadAllLocalPokemonListSmallerThan(
            targetNum = targetNum,
            onSuccess = {
                pokemonListFlow.value = it
                lastLoadPokemonNum = it.last().pokemon.getPokemonNum()
            }
        )
    }

    suspend fun renewPokemonList() {
        if (pokemonListFlow.value.isEmpty()) return
        loadAllLocalPokemonListSmallerThan(lastLoadPokemonNum)
    }

    private suspend fun savePokemonToLocalDB(pokemonEntity: PokemonEntity) {
        pokemonRepository.savePokemonToLocalDB(pokemonEntity)
        renewPokemonList()
    }

    private suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        pokemonRepository.updatePokemonHeart(targetPokemonNum, heartValue)
        renewPokemonList()
    }

    fun plusBtnClickListener() = viewModelScope.launch {
        savePokemonToLocalDB(
            getRandomDummyEntity(pokemonListFlow.value.size + 1)
        )
    }

    fun loadNextPokemonList(lastVisibleItemPosition: Int) {
        if (isLoading.value || isPokemonLastData ||
            pokemonListFlow.value.size - 1 > lastVisibleItemPosition
        ) return
        viewModelScope.launch { loadRemotePokemonList(lastLoadPokemonNum) }
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