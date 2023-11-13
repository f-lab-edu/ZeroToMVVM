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
    private val repository: PokemonRepository
) : MainContract.Presenter {

    private val activity = view as MainActivity

    override fun loadPokemonList() {
        activity.lifecycleScope.launch {
            runCatching { repository.loadPokemonList() }
                .onFailure { activity.showToast("data load를 실패했습니다. : ${it.message}") }
        }
    }

    override fun updatePokemonList(newList: List<PokemonListItem>) {
        activity.lifecycleScope.launch {
            repository.pokemonListFlow.emit(newList)
        }
    }

    override fun deletePokemonInWishItem(selectedItem: PokemonListItem) {
        activity.lifecycleScope.launch {
            val currentList = repository.pokemonListFlow.first().toMutableList()
            currentList.forEachIndexed { index, pokemonListItem ->
                if (pokemonListItem.pokemon.name != selectedItem.pokemon.name) return@forEachIndexed
                currentList[index] = selectedItem.copy(heart = false)
            }
            repository.pokemonListFlow.emit(currentList)
        }
    }

    override fun observePokemonList(callback: (List<PokemonListItem>) -> Unit) {
        activity.lifecycleScope.launch {
            repository.pokemonListFlow.collect { callback(it) }
        }
    }

    override fun observeWishPokemonList(callback: (List<PokemonListItem>) -> Unit) {
        activity.lifecycleScope.launch {
            repository.wishPokemonListFlow.collect { callback(it) }
        }
    }

}