package com.kova700.zerotomvvm.view.main.home.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import kotlinx.coroutines.CoroutineScope

interface HomeContract {
    interface View {
        val lifecycleScope: CoroutineScope
        fun moveToDetail(selectedItem: PokemonListItem)
        fun showToast(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        var isPokemonLoading: Boolean
        var isPokemonLastData: Boolean
        suspend fun loadRemotePokemonList(offset: Int = 0)
        suspend fun loadAllLocalPokemonListSmallerThan(targetNum :Int)
        suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean)
        suspend fun savePokemonToLocalDB(pokemonEntity: PokemonEntity)
    }
}