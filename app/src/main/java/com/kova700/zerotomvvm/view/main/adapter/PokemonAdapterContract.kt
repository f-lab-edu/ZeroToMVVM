package com.kova700.zerotomvvm.view.main.adapter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

interface PokemonAdapterContract {
    interface View {
        var onHeartClick: ((Int) -> Unit)?
        var onItemClick: ((Int) -> Unit)?
    }

    interface Model {
        fun getCurrentList(): List<PokemonListItem>
        fun submitItemList(list: List<PokemonListItem>)
        fun addRandomItem()
    }
}