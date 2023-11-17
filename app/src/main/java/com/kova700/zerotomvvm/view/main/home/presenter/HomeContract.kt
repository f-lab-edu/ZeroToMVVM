package com.kova700.zerotomvvm.view.main.home.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

interface HomeContract {
    interface View {
        fun moveToDetail(selectedItem: PokemonListItem)
        fun showToast(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        suspend fun loadRemotePokemonList()
        suspend fun loadLocalPokemonList()
        fun addRandomItem()
        fun updatePokemonList(newList: List<PokemonListItem>)
        fun renewPokemonList()
    }
}