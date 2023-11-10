package com.kova700.zerotomvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kova700.zerotomvvm.FragmentTags.HOME_FRAGMENT_TAG
import com.kova700.zerotomvvm.FragmentTags.WISH_FRAGMENT_TAG
import com.kova700.zerotomvvm.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var binding: ActivityMainBinding

    private val pokemonRepository: PokemonRepository by lazy { PokemonRepositoryImpl(PokemonApi.service) }

    val pokeymonListFlow: MutableStateFlow<List<PokemonListItem>> = MutableStateFlow(listOf())
    val wishPokeymonListFlow: Flow<List<PokemonListItem>>
        get() = pokeymonListFlow.map { list -> list.filter { it.heart } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragmentContainer(savedInstanceState)
        initBottomNavigationView()
        getRemotePokemonList()
    }

    //TODO :예외처리 추가
    private fun getRemotePokemonList() = lifecycleScope.launch {
        val data = pokemonRepository.getPokemonList()
            .map { pokemon: Pokemon -> PokemonListItem(pokemon, false) }
        pokeymonListFlow.emit(data)
    }

    fun updatePokemonList(data: List<PokemonListItem>) = lifecycleScope.launch {
        pokeymonListFlow.emit(data)
    }

    fun deletePokemonItem(selectedItem: PokemonListItem) = lifecycleScope.launch {
        val currentList = pokeymonListFlow.first().toMutableList()
        currentList.forEachIndexed { index, pokemonListItem ->
            if (pokemonListItem.pokemon.name != selectedItem.pokemon.name) return@forEachIndexed
            currentList[index] = selectedItem.copy(heart = false)
        }
        pokeymonListFlow.emit(currentList)
    }

    private fun initBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_menu_home -> showFragment(HOME_FRAGMENT_TAG)
                R.id.bottom_menu_wish -> showFragment(WISH_FRAGMENT_TAG)
            }
            true
        }
    }

    private fun initFragmentContainer(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return
        showFragment(HOME_FRAGMENT_TAG)
    }

    private fun showFragment(tag: FragmentTags) {
        val targetFragment = getFragmentInstanceByTag(tag)

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.container_main, targetFragment, tag.name)
            .commitNow()
    }

    companion object {
        const val TO_DETAIL_SELECTED_ITEM_EXTRA = "TO_DETAIL_SELECTED_ITEM_EXTRA"
        const val TO_DETAIL_ITEM_POSITION_EXTRA = "TO_DETAIL_ITEM_POSITION_EXTRA"
    }
}