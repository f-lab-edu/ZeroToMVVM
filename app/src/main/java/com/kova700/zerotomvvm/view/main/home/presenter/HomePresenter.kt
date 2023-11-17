package com.kova700.zerotomvvm.view.main.home.presenter

import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.getRandomDummyItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import com.kova700.zerotomvvm.view.main.adapter.PokemonAdapterContract

class HomePresenter(
    private val view: HomeContract.View,
    private val adapterView: PokemonAdapterContract.View,
    private val adapterModel: PokemonAdapterContract.Model,
    private val repository: PokemonRepository
) : HomeContract.Presenter {

    init {
        adapterView.onItemClick = { itemPosition ->
            itemClickListener(itemPosition)
        }

        adapterView.onHeartClick = { itemPosition ->
            heartClickListener(itemPosition)
        }
        view.showLoading()
    }

    override suspend fun loadRemotePokemonList() {
        runCatching { repository.loadRemotePokemonList() }
            .onSuccess { adapterModel.submitItemList(it) }
            .onFailure {
                loadLocalPokemonList()
                view.showToast("서버로부터 데이터 load를 실패했습니다. : ${it.message}")
            }
            .also { view.hideLoading() }
    }

    override suspend fun loadLocalPokemonList() {
        runCatching { repository.loadLocalPokemonList() }
            .onSuccess { adapterModel.submitItemList(it) }
    }

    override fun addRandomItem() {
        val newList = adapterModel.getCurrentList().toMutableList().apply {
            add(getRandomDummyItem(adapterModel.getCurrentList().size + 1))
        }
        updatePokemonList(newList)
    }

    override fun renewPokemonList() {
        adapterModel.submitItemList(repository.pokemonList)
    }

    override fun updatePokemonList(newList: List<PokemonListItem>) {
        repository.pokemonList = newList
        adapterModel.submitItemList(newList)
    }

    private fun heartClickListener(itemPosition: Int) {
        val newList = adapterModel.getCurrentList().toMutableList().apply {
            val selectedItem = this[itemPosition]
            this[itemPosition] = this[itemPosition].copy(heart = selectedItem.heart.not())
        }
        updatePokemonList(newList)
    }

    private fun itemClickListener(itemPosition: Int) {
        view.moveToDetail(adapterModel.getCurrentList()[itemPosition])
    }
}