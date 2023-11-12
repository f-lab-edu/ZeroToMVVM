package com.kova700.zerotomvvm.view.main.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

interface MainContract {
    interface View {}
    interface Presenter {
        fun loadPokemonList()
        fun updatePokemonList(newList : List<PokemonListItem>)
        fun deletePokemonItem(selectedItem: PokemonListItem)
        fun observePokemonList(callback: (List<PokemonListItem>) -> Unit)
        fun observeWishPokemonList(callback: (List<PokemonListItem>) -> Unit)
    }
}