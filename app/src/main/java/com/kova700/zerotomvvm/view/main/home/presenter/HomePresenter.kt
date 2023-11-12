package com.kova700.zerotomvvm.view.main.home.presenter

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.view.detail.DetailActivity
import com.kova700.zerotomvvm.view.main.MainActivity
import com.kova700.zerotomvvm.view.main.MainActivity.Companion.TO_DETAIL_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.view.main.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_EXTRA
import com.kova700.zerotomvvm.view.main.adapter.PokemonAdapterContract
import com.kova700.zerotomvvm.view.main.home.HomeFragment
import kotlinx.coroutines.launch

class HomePresenter(
    private val view: HomeContract.View,
    private val adapterView: PokemonAdapterContract.View,
    private val adapterModel: PokemonAdapterContract.Model,
) : HomeContract.Presenter {

    private val fragment = view as HomeFragment
    private val activity = fragment.requireActivity() as MainActivity

    init {
        adapterView.onItemClick = { itemPosition ->
            itemClickListener(itemPosition)
        }

        adapterView.onHeartClick = { itemPosition ->
            heartClickListener(itemPosition)
        }
    }

    override fun observePokemonList() {
        fragment.viewLifecycleOwner.lifecycleScope.launch {
            activity.presenter.observePokemonList {
                adapterModel.submitItemList(it)
            }
        }
    }

    override fun updatePokemonList(newList: List<PokemonListItem>) {
        activity.presenter.updatePokemonList(newList)
    }

    override fun heartClickListener(itemPosition: Int) {
        val newList = adapterModel.getCurrentList().toMutableList().apply {
            val selectedItem = this[itemPosition]
            this[itemPosition] = this[itemPosition].copy(heart = !selectedItem.heart)
        }
        updatePokemonList(newList)
    }

    override fun itemClickListener(itemPosition: Int) {
        val selectedItem = adapterModel.getCurrentList()[itemPosition]
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(TO_DETAIL_SELECTED_ITEM_EXTRA, selectedItem)
            putExtra(TO_DETAIL_ITEM_POSITION_EXTRA, itemPosition)
        }
        fragment.activityResultLauncher.launch(intent)
    }

    override fun addRandomItem() {
        adapterModel.addRandomItem()
    }

}