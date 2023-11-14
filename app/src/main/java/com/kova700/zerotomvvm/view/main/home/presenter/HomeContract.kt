package com.kova700.zerotomvvm.view.main.home.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

interface HomeContract {
    interface View {
        fun moveToDetail(itemPosition: Int, selectedItem: PokemonListItem)
        fun showToast(message: String)
    }

    interface Presenter {
        suspend fun loadPokemonList()
        fun addRandomItem()
        fun updateHeartInPosition(itemPosition: Int, heartValue: Boolean)
        fun updatePokemonList(newList: List<PokemonListItem>)
    }
}