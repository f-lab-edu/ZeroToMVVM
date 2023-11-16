package com.kova700.zerotomvvm.view.detail.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository

class DetailPresenter(
    val view: DetailContract.View,
    val repository: PokemonRepository
) : DetailContract.Presenter {

    override fun updateItemData(newItem: PokemonListItem) {
        val newList = repository.pokemonList.toMutableList()
        val index = newList.indexOfFirst { it.pokemon.name == newItem.pokemon.name }
        if (index == -1) return

        newList[index] = newItem
        repository.pokemonList = newList
    }

    fun heartClickListener(selectedItem: PokemonListItem) {
        val newItem = selectedItem.copy(heart = selectedItem.heart.not())
        updateItemData(newItem)
    }

}