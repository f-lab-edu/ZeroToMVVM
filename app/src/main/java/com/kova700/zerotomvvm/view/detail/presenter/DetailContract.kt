package com.kova700.zerotomvvm.view.detail.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import kotlinx.coroutines.CoroutineScope

interface DetailContract {
    interface View {
        val lifecycleScope: CoroutineScope
    }

    interface Presenter {
        suspend fun updateItemData(selectedItem: PokemonListItem)
    }
}