package com.kova700.zerotomvvm.view.main.wish.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

interface WishContract {
    interface View {
        fun moveToDetail(selectedItem: PokemonListItem)
    }

    interface Presenter {
        fun loadWishPokemonList()
        fun updatePokemonList(newList: List<PokemonListItem>)
        fun renewPokemonList()
    }
}