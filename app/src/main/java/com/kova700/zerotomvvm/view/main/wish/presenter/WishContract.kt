package com.kova700.zerotomvvm.view.main.wish.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import kotlinx.coroutines.CoroutineScope

interface WishContract {
    interface View {
        val lifecycleScope: CoroutineScope
        fun moveToDetail(selectedItem: PokemonListItem)
    }

    interface Presenter {
        suspend fun loadLocalWishPokemonList()
        suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean)
    }
}