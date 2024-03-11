package com.kova700.zerotomvvm.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kova700.zerotomvvm.R
import com.kova700.zerotomvvm.ui.main.model.PokemonListItem
import com.kova700.zerotomvvm.databinding.ItemPokemonListBinding
import javax.inject.Inject

class PokemonListAdapter @Inject constructor() :
    ListAdapter<PokemonListItem, PokemonListViewHolder>(PokemonListItemDiffUtil()) {
    var onHeartClick: ((Int) -> Unit)? = null
    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        val binding =
            ItemPokemonListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonListViewHolder(binding, onItemClick, onHeartClick)
    }

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_pokemon_list
}