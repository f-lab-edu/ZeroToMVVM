package com.kova700.zerotomvvm.data.source.pokemon.remote

import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.api.PokemonApi.Companion.GET_POKEMON_API_PAGING_SIZE
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonDao
import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
import com.kova700.zerotomvvm.data.source.pokemon.toDBEntity
import com.kova700.zerotomvvm.data.source.pokemon.toListItem

class PokemonRepositoryImpl private constructor(
    private val pokemonService: PokemonApi,
    private val pokemonDao: PokemonDao
) : PokemonRepository {
    override var lastLoadPokemonNum: Int = 0 //replace시에 마지막으로 load 했던 아이템 번호 기억하기 위해

    override suspend fun loadRemotePokemonList(
        offset: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onSuccess: (List<PokemonListItem>) -> Unit,
        onFailure: (Throwable) -> Unit,
        onLastData: () -> Unit,
    ) {
        onStart()
        //런타임에 데이터를 가져온 적이 있다면 해당 분기문 작동
        //configuration change, replace 상황을 위해서 작성한 부분
        if (lastLoadPokemonNum >= offset + GET_POKEMON_API_PAGING_SIZE) {
            loadAllLocalPokemonListSmallerThan(
                targetNum = lastLoadPokemonNum,
                onSuccess = onSuccess,
            )
            onComplete()
            return
        }

        //로컬 DB에 저장 여부와 상관없이 처음엔 무조건 서버 API요청하게 구현함
        runCatching { pokemonService.getPokemon(offset = offset) }
            .onSuccess {
                if (it.next.isNullOrBlank()) onLastData()
                savePokemonListToLocalDB(it.results.toDBEntity())
                lastLoadPokemonNum = it.results.last().getPokemonNum()
                loadAllLocalPokemonListSmallerThan(
                    targetNum = lastLoadPokemonNum,
                    onSuccess = onSuccess,
                )
                onComplete()
            }
            .onFailure { onFailure(it) }
    }

    override suspend fun loadAllLocalPokemonListSmallerThan(
        targetNum: Int,
        onSuccess: (List<PokemonListItem>) -> Unit
    ) {
        runCatching { pokemonDao.getAllPokemonListSmallerThan(targetNum).toListItem() }
            .onSuccess { onSuccess(it) }
    }


    //TODO : onFailure부분 사실 없어도 될 것 같음
    override suspend fun loadLocalWishPokemonList(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onSuccess: (List<PokemonListItem>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        onStart()
        runCatching { pokemonDao.getPokemonListFromHeart(true).toListItem() }
            .onSuccess { onSuccess(it) }
            .onFailure { onFailure(it) }
        onComplete()
    }

    override suspend fun savePokemonListToLocalDB(pokemonList: List<PokemonEntity>) {
        pokemonDao.insertPokemonList(pokemonList)
    }

    override suspend fun savePokemonToLocalDB(pokemon: PokemonEntity) {
        pokemonDao.insertPokemon(pokemon)
    }

    override suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean) {
        pokemonDao.updatePokemonHeart(targetPokemonNum, heartValue)
    }

    companion object {
        @Volatile
        private var INSTANCE: PokemonRepository? = null

        fun getInstance(pokemonService: PokemonApi, appDatabase: AppDataBase): PokemonRepository =
            INSTANCE ?: synchronized(this) {
                PokemonRepositoryImpl(
                    pokemonService,
                    appDatabase.pokemonDao()
                ).apply { INSTANCE = this }
            }
    }
}