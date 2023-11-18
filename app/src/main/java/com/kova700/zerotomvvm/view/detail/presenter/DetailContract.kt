package com.kova700.zerotomvvm.view.detail.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

interface DetailContract {
    interface View {}
    interface Presenter {
        fun updateItemData(newItem: PokemonListItem)
    }
}