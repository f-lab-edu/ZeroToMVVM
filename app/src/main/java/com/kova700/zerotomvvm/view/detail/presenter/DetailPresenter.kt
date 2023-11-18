package com.kova700.zerotomvvm.view.detail.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import kotlinx.coroutines.launch

class DetailPresenter(
    val view: DetailContract.View,
    val repository: PokemonRepository
) : DetailContract.Presenter {

    override suspend fun updateItemData(selectedItem: PokemonListItem) {
        repository.updatePokemonHeart(
            selectedItem.pokemon.getPokemonNum(),
            selectedItem.heart.not()
        )
    }

    fun heartClickListener(selectedItem: PokemonListItem) {
        view.lifecycleScope.launch {
            updateItemData(selectedItem)
        }
    }

}