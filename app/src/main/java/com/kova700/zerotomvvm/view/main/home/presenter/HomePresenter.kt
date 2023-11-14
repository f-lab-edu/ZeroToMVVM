package com.kova700.zerotomvvm.view.main.home.presenter

import android.content.ClipData.Item
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
    }

    //TODO : 로딩 상태 추가,
    override suspend fun loadPokemonList() {
        runCatching { repository.loadPokemonList() }
            .onSuccess { adapterModel.submitItemList(it) }
            .onFailure { view.showToast("data load를 실패했습니다. : ${it.message}") }
    }

    override fun addRandomItem() {
        val newList = adapterModel.getCurrentList().toMutableList().apply {
            add(getRandomDummyItem(adapterModel.getCurrentList().size + 1))
        }
        updatePokemonList(newList)
    }

    override fun updateHeartInPosition(itemPosition: Int, heartValue: Boolean) {
        val newList = adapterModel.getCurrentList().toMutableList().apply {
            this[itemPosition] = this[itemPosition].copy(heart = heartValue)
        }
        updatePokemonList(newList)
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
        view.moveToDetail(itemPosition, adapterModel.getCurrentList()[itemPosition])
    }
}