package com.kova700.zerotomvvm.view.main.home.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

interface HomeContract {
    interface View {}
    interface Presenter {
        fun observePokemonList()
        fun updatePokemonList(newList: List<PokemonListItem>)
        fun heartClickListener(itemPosition: Int)
        fun itemClickListener(itemPosition: Int)
        fun addRandomItem()
    }
}