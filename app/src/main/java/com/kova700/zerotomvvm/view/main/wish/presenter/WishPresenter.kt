package com.kova700.zerotomvvm.view.main.wish.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import com.kova700.zerotomvvm.view.main.adapter.PokemonAdapterContract

class WishPresenter(
    private val view: WishContract.View,
    private val adapterView: PokemonAdapterContract.View,
    private val adapterModel: PokemonAdapterContract.Model,
    private val repository: PokemonRepository
) : WishContract.Presenter {

    init {
        adapterView.onItemClick = { itemPosition ->
            itemClickListener(itemPosition)
        }

        adapterView.onHeartClick = { itemPosition ->
            heartClickListener(itemPosition)
        }
    }

    override fun loadWishPokemonList() {
        adapterModel.submitItemList(repository.wishPokemonList)
    }

    override fun deleteInWishPosition(itemPosition: Int) {
        val selectedItem = adapterModel.getCurrentList()[itemPosition]
        val newList = repository.pokemonList.toMutableList()
        newList.forEachIndexed { index, pokemonListItem ->
            if (pokemonListItem.pokemon.name != selectedItem.pokemon.name) return@forEachIndexed
            newList[index] = selectedItem.copy(heart = false)
        }
        updatePokemonList(newList)
    }

    override fun updatePokemonList(newList: List<PokemonListItem>) {
        repository.pokemonList = newList
        adapterModel.submitItemList(repository.wishPokemonList)
    }

    private fun itemClickListener(itemPosition: Int) {
        view.moveToDetail(itemPosition, adapterModel.getCurrentList()[itemPosition])
    }

    private fun heartClickListener(itemPosition: Int) {
        val selectedItem = adapterModel.getCurrentList()[itemPosition]
        if (selectedItem.heart.not()) return
        deleteInWishPosition(itemPosition)
    }

}