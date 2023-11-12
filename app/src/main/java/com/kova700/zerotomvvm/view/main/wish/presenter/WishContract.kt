package com.kova700.zerotomvvm.view.main.wish.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

interface WishContract {
    interface View {}
    interface Presenter {
        fun observeWishPokemonList()
        fun deletePokemonInWishItem(selectedItem: PokemonListItem)
        fun itemClickListener(itemPosition: Int)
        fun heartClickListener(itemPosition: Int)
    }
}