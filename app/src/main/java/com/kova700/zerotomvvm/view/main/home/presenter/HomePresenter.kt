package com.kova700.zerotomvvm.view.main.home.presenter

import com.kova700.zerotomvvm.data.api.PokemonApi.Companion.GET_POKEMON_API_PAGING_SIZE
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
    override var isPokemonLoading: Boolean = false
    override var isPokemonLastData: Boolean = false

    init {
        adapterView.onItemClick = { itemPosition ->
            itemClickListener(itemPosition)
        }

        adapterView.onHeartClick = { itemPosition ->
            heartClickListener(itemPosition)
        }
    }

    override suspend fun loadRemotePokemonList(offset: Int) {
        repository.loadRemotePokemonList(
            offset = offset,
            onStart = { showLoading() },
            onComplete = { hideLoading() },
            onSuccess = { adapterModel.submitItemList(it) },
            onFailure = { loadRemotePokemonListFailCallback(it) },
            onLastData = { isPokemonLastData = true }
        )
    }

    override suspend fun loadAllLocalPokemonListSmallerThan(targetNum: Int) {
        repository.loadAllLocalPokemonListSmallerThan(
            targetNum = targetNum,
            onSuccess = {
                adapterModel.submitItemList(it)
                repository.lastLoadPokemonNum = it.last().pokemon.getPokemonNum()
            }
        )
    }

    suspend fun renewPokemonList() {
        if (adapterModel.getCurrentList().isEmpty()) return
        loadAllLocalPokemonListSmallerThan(repository.lastLoadPokemonNum)
    }

    override suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        repository.updatePokemonHeart(targetPokemonNum, heartValue)
        renewPokemonList()
    }

    override suspend fun savePokemonToLocalDB(pokemonEntity: PokemonEntity) {
        repository.savePokemonToLocalDB(pokemonEntity)
        renewPokemonList()
    }

    //서버로부터 API요청이 실패하면 이전에 로컬 DB에 저장해놨던 데이터를 가져옴
    private fun loadRemotePokemonListFailCallback(throwable: Throwable) {
        view.lifecycleScope.launch {
            view.showToast("서버로부터 데이터 load를 실패했습니다. : ${throwable.message}")
            loadAllLocalPokemonListSmallerThan(
                repository.lastLoadPokemonNum + GET_POKEMON_API_PAGING_SIZE
            )
        }
    }

    fun plusBtnClickListener() {
        view.lifecycleScope.launch {
            savePokemonToLocalDB(
                getRandomDummyEntity(adapterModel.getCurrentList().size + 1)
            )
        }
    }

    private fun heartClickListener(itemPosition: Int) {
        view.lifecycleScope.launch {
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

    private fun showLoading() {
        isPokemonLoading = true
        view.showLoading()
    }

    private fun hideLoading() {
        isPokemonLoading = false
        view.hideLoading()
    }

    fun loadNextPokemonList(lastVisibleItemPosition: Int) {
        if (isPokemonLoading || isPokemonLastData ||
            adapterModel.getCurrentList().size - 1 > lastVisibleItemPosition
        //뭔가 추가되어야 할 듯한 느낌인데, 동시에 호출되는 경우가 생각보다 좀 있는 거 같은데
        // 어댑터에 로드된 데이터가 반영되지 않아서 이미 API를 통해서 데이터를 가져왔음에도
        // adapterModel.getCurrentList().size - 1 > lastVisibleItemPosition를 포함한 위 상황이 전부 True가 되어서
        // 해당 if문을 통과하는 상황이 생김
        ) return
        view.lifecycleScope.launch {
            loadRemotePokemonList(repository.lastLoadPokemonNum)
        }
    }
}