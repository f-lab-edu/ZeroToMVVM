package com.kova700.zerotomvvm.view.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kova700.zerotomvvm.R
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.local.getRandomDummyItem
import com.kova700.zerotomvvm.databinding.ItemPokemonListBinding

class PokemonListAdapter() :
    ListAdapter<PokemonListItem, PokemonListViewHolder>(PokemonListItemDiffUtil()),
    PokemonAdapterContract.View, PokemonAdapterContract.Model {

    override var onHeartClick: ((Int) -> Unit)? = null
    override var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        val binding =
            ItemPokemonListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonListViewHolder(binding, onItemClick, onHeartClick)
    }

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_pokemon_list

    override fun submitItemList(list: List<PokemonListItem>) {
        super.submitList(list)
    }

    override fun addRandomItem() {
        val newList = currentList.toMutableList().apply {
            add(getRandomDummyItem(currentList.size + 1))
        }
        super.submitList(newList)
    }

}