package com.kova700.zerotomvvm.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kova700.zerotomvvm.R
import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepositoryImpl
import com.kova700.zerotomvvm.databinding.ActivityMainBinding
import com.kova700.zerotomvvm.util.FragmentTags
import com.kova700.zerotomvvm.util.FragmentTags.HOME_FRAGMENT_TAG
import com.kova700.zerotomvvm.util.FragmentTags.WISH_FRAGMENT_TAG
import com.kova700.zerotomvvm.util.getFragmentInstanceByTag
import com.kova700.zerotomvvm.view.main.presenter.MainContract
import com.kova700.zerotomvvm.view.main.presenter.MainPresenter

class MainActivity : AppCompatActivity(R.layout.activity_main), MainContract.View {
    private lateinit var binding: ActivityMainBinding
    val presenter: MainContract.Presenter by lazy {
        MainPresenter(
            this@MainActivity,
            PokemonRepositoryImpl(PokemonApi.service)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragmentContainer(savedInstanceState)
        initBottomNavigationView()
        presenter.loadPokemonList()
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