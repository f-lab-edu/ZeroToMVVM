package com.kova700.zerotomvvm.view.main.home.presenter

import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import com.kova700.zerotomvvm.data.source.pokemon.local.getRandomDummyEntity
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepository
import com.kova700.zerotomvvm.view.main.adapter.PokemonAdapterContract
import kotlinx.coroutines.launch

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
            .onFailure { loadRemotePokemonListFailCallback(it) }
            .also { view.hideLoading() }
    }

    override suspend fun loadLocalPokemonList() {
        runCatching { repository.loadLocalPokemonList() }
            .onSuccess { adapterModel.submitItemList(it) }
    }

    override suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        repository.updatePokemonHeart(targetPokemonNum, heartValue)
        loadLocalPokemonList()
    }

    override suspend fun savePokemonToLocalDB(pokemonEntity: PokemonEntity) {
        repository.savePokemonToLocalDB(pokemonEntity)
        loadLocalPokemonList()
    }

    private suspend fun loadRemotePokemonListFailCallback(throwable: Throwable) {
        loadLocalPokemonList()
        view.showToast("서버로부터 데이터 load를 실패했습니다. : ${throwable.message}")
    }

    fun plusBtnClickListener() {
        view.viewLifecycleScope.launch {
            savePokemonToLocalDB(
                getRandomDummyEntity(adapterModel.getCurrentList().size + 1)
            )
        }
    }

    private fun heartClickListener(itemPosition: Int) {
        view.viewLifecycleScope.launch {
            val selectedItem = adapterModel.getCurrentList()[itemPosition]
            updatePokemonHeart(
                selectedItem.pokemon.getPokemonNum(),
                selectedItem.heart.not()
            )
        }
    }

    private fun itemClickListener(itemPosition: Int) {
        view.moveToDetail(adapterModel.getCurrentList()[itemPosition])
    }
}