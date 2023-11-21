package com.kova700.zerotomvvm.view.main.wish.presenter

import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import com.kova700.zerotomvvm.view.main.adapter.PokemonAdapterContract
import kotlinx.coroutines.launch

class WishPresenter(
    private val view: WishContract.View,
    private val adapterView: PokemonAdapterContract.View,
    private val adapterModel: PokemonAdapterContract.Model,
    private val repository: PokemonRepository
) : WishContract.Presenter {

    init {
        adapterView.onItemClick = { itemPosition ->
            itemClickListener(itemPosition)
        }

        adapterView.onHeartClick = { itemPosition ->
            heartClickListener(itemPosition)
        }
    }

    override suspend fun loadLocalWishPokemonList() {
        repository.loadLocalWishPokemonList(
            onStart = { view.showLoading() },
            onComplete = { view.hideLoading() },
            onSuccess = { adapterModel.submitItemList(it) },
            onFailure = { loadLocalWishPokemonListFailCallback(it) },
        )
    }

    private fun loadLocalWishPokemonListFailCallback(throwable: Throwable) {
        view.showToast("Wish 데이터 load를 실패했습니다. : ${throwable.message}")
    }

    suspend fun renewPokemonList() {
        if (adapterModel.getCurrentList().isEmpty()) return
        loadLocalWishPokemonList()
    }

    override suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        repository.updatePokemonHeart(targetPokemonNum, heartValue)
        loadLocalWishPokemonList()
    }

    private fun itemClickListener(itemPosition: Int) {
        view.moveToDetail(adapterModel.getCurrentList()[itemPosition])
    }

    private fun heartClickListener(itemPosition: Int) {
        view.lifecycleScope.launch {
            val selectedItem = adapterModel.getCurrentList()[itemPosition]
            if (selectedItem.heart.not()) return@launch

            updatePokemonHeart(
                selectedItem.pokemon.getPokemonNum(),
                false
            )
        }
    }

}