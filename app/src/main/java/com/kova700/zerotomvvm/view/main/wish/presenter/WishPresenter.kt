package com.kova700.zerotomvvm.view.main.wish.presenter

import android.content.Intent
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.view.detail.DetailActivity
import com.kova700.zerotomvvm.view.main.MainActivity
import com.kova700.zerotomvvm.view.main.adapter.PokemonAdapterContract
import com.kova700.zerotomvvm.view.main.wish.WishFragment

class WishPresenter(
    private val view: WishContract.View,
    private val adapterView: PokemonAdapterContract.View,
    private val adapterModel: PokemonAdapterContract.Model,
) : WishContract.Presenter {

    init {
        adapterView.onItemClick = { itemPosition ->
            itemClickListener(itemPosition)
        }

        adapterView.onHeartClick = { itemPosition ->
            heartClickListener(itemPosition)
        }
    }

    private val fragment = view as WishFragment
    private val activity = fragment.requireActivity() as MainActivity

    override fun observeWishPokemonList() {
        activity.presenter.observeWishPokemonList {
            adapterModel.submitItemList(it)
        }
    }

    override fun deletePokemonInWishItem(selectedItem: PokemonListItem) {
        activity.presenter.deletePokemonInWishItem(selectedItem)
    }

    override fun itemClickListener(itemPosition: Int) {
        val selectedItem = adapterModel.getCurrentList()[itemPosition]
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(MainActivity.TO_DETAIL_SELECTED_ITEM_EXTRA, selectedItem)
            putExtra(MainActivity.TO_DETAIL_ITEM_POSITION_EXTRA, itemPosition)
        }
        fragment.activityResultLauncher.launch(intent)
    }

    override fun heartClickListener(itemPosition: Int) {
        val selectedItem = adapterModel.getCurrentList()[itemPosition]
        if (selectedItem.heart) deletePokemonInWishItem(selectedItem)
    }

}