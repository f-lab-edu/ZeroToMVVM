package com.kova700.zerotomvvm.view.main.presenter

import androidx.lifecycle.lifecycleScope
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import com.kova700.zerotomvvm.util.showToast
import com.kova700.zerotomvvm.view.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainPresenter(
    private val view: MainContract.View,
    private val mainRepository: PokemonRepository
    //어댑터를 또 Model 형식과 View형식으로 찢어야함
    //근데 어댑터가 HomeFragment에서 쓰는 게 있고, WishFragment에서 쓰는게 있는데
    //여기에 전부 가져와야할까?
    //아니면 각 Fragment별로 Presenter를 따로 만든다던가
) : MainContract.Presenter {

    private val activity = view as MainActivity

    override fun loadPokemonList() {
        activity.lifecycleScope.launch {
            runCatching { mainRepository.loadPokemonList() }
                .onFailure { activity.showToast("data load를 실패했습니다. : ${it.message}") }
        }
    }

    override fun updatePokemonList(newList: List<PokemonListItem>) {
        activity.lifecycleScope.launch {
            mainRepository.pokemonListFlow.emit(newList)
        }
    }

    override fun deletePokemonItem(selectedItem: PokemonListItem) {
        activity.lifecycleScope.launch {
            val currentList = mainRepository.pokemonListFlow.first().toMutableList()
            currentList.forEachIndexed { index, pokemonListItem ->
                if (pokemonListItem.pokemon.name != selectedItem.pokemon.name) return@forEachIndexed
                currentList[index] = selectedItem.copy(heart = false)
            }
            mainRepository.pokemonListFlow.emit(currentList)
        }
    }

    override fun observePokemonList(callback: (List<PokemonListItem>) -> Unit) {
        activity.lifecycleScope.launch {
            mainRepository.pokemonListFlow.collect { callback(it) }
        }
    }

    override fun observeWishPokemonList(callback: (List<PokemonListItem>) -> Unit) {
        activity.lifecycleScope.launch {
            mainRepository.wishPokemonListFlow.collect { callback(it) }
        }
    }

}