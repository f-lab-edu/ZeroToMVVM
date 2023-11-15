package com.kova700.zerotomvvm.view.main.home.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

interface HomeContract {
    interface View {
        fun moveToDetail(selectedItem: PokemonListItem)
        fun showToast(message: String)
    }

    interface Presenter {
        suspend fun loadPokemonList()
        fun addRandomItem()
        fun updatePokemonList(newList: List<PokemonListItem>)
        fun renewPokemonList()
    }
}