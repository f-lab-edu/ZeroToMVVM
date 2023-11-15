package com.kova700.zerotomvvm.view.detail.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository

class DetailPresenter(
    val view: DetailContract.View,
    val repository: PokemonRepository
) : DetailContract.Presenter {

    override fun updateItemData(pokemon: PokemonListItem) {
        val newList = repository.pokemonList.toMutableList()
        newList.forEachIndexed { index, pokemonListItem ->
            if (pokemonListItem.pokemon.name != pokemon.pokemon.name) return@forEachIndexed
            newList[index] = pokemon
        }
        repository.pokemonList = newList
    }

    fun heartClickListener(selectedItem: PokemonListItem) {
        val newItem = selectedItem.copy(heart = selectedItem.heart.not())
        updateItemData(newItem)
    }

}