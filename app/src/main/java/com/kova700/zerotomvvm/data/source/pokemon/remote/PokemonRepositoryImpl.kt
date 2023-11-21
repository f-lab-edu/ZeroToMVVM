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
    //TODO : 매번 서버로 부터 데이터를 가져와서 덮어쓰기를 하지만, (로컬 DB 데이터들과 서버 데이터 불일치 해결을 위해)
    // 네트워크가 끊겼다면, 서버로 부터 데이터를 가져올 수 없음으로,
    // 이전에 로컬DB에 저장해놨던 데이터를 페이징해서 보여준다.
    // 그 와중에 네트워크가 연결된다면, 이전에 마지막으로 서버로부터 가져왔던 데이터부터 다시 페이징을 보낸다.
    // 어쩔 수 없이 화면은 마지막으로 서버로부터 가져왔던 데이터부터 보여지게 수정되겠지만,,,,
    // 이를 위해서 로컬에서 가져온 마지막 번호를 표기하는 localLastLoadPokemonNum변수와
    // 서버에서 가져온 마지막 번호를 표기하는 remoteLastLoadPokemonNum변수를 분리해서 가져가야할 듯하다.
    // loadRemotePokemonList()가 호출될 때,
    // remoteLastLoadPokemonNum로 API 요청을 매번 보내고, 네트워크가 없으면 요청이 실패하니까
    // HomePresenter의 loadRemotePokemonListFailCallback에서 localLastLoadPokemonNum을 가지고,
    // localLastLoadPokemonNum값을 증가시키면서 로컬에서 데이터를 가져오면 될듯,
    // 중간에 네트워크가 다시 연결된다면 loadRemotePokemonList()의 요청이 성공할 테니까
    // remoteLastLoadPokemonNum까지의 데이터를 보여주고,
    // localLastLoadPokemonNum을 remoteLastLoadPokemonNum 값으로 통일

    // 두 개가 다르다면, 작은걸로 서버에게 요청 보내는 방식으로 쓰면 될듯?

    override suspend fun loadRemotePokemonList(
        offset: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onSuccess: (List<PokemonListItem>) -> Unit,
        onFailure: (Throwable) -> Unit,
        onLastData: () -> Unit,
    ) {
        onStart()
        //이미 런타임에 서버로부터 데이터를 가져온 적이 있다면 해당 분기문 들어감
        //요청한 offset + 페이징size가 lastLoadPokemonNum보다 작다면 lastLoadPokemonNum만큼 로컬 DB에서 가져다 주는 방식
        //==> configuration change, replace시에도 적합해 보인다.
        if (lastLoadPokemonNum >= offset + GET_POKEMON_API_PAGING_SIZE) {
            onSuccess(loadAllLocalPokemonListSmallerThan(lastLoadPokemonNum))
            onComplete()
            return
        }

        //처음엔 로컬DB 저장 유무와 상관없이 서버로부터 그냥 가져오는 게 맞음
        runCatching { pokemonService.getPokemon(offset = offset) }
            .onSuccess {
                if (it.next.isNullOrBlank()) onLastData()
                savePokemonListToLocalDB(it.results.toDBEntity())
                lastLoadPokemonNum = it.results.last().getPokemonNum()
                onSuccess(loadAllLocalPokemonListSmallerThan(lastLoadPokemonNum))
                onComplete()
            }
            .onFailure { onFailure(it) }
    }

    override suspend fun loadAllLocalPokemonListSmallerThan(targetNum: Int): List<PokemonListItem> {
        return pokemonDao.getAllPokemonListSmallerThan(targetNum).toListItem()
    }

    //TODO : 이거도 지금까지 가져온 애들중에서 Wish인 애들 가져오게 수정해야 함
    // 현재는 offset부터 30개만 가져오게 되어있음
    override suspend fun loadLocalWishPokemonList(offset: Int): List<PokemonListItem> {
        return pokemonDao.getPokemonListFromHeart(offset, true).toListItem()
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