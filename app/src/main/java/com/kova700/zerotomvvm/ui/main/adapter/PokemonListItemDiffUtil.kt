package com.kova700.zerotomvvm.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kova700.zerotomvvm.ui.main.model.PokemonListItem

class PokemonListItemDiffUtil : DiffUtil.ItemCallback<PokemonListItem>() {
    override fun areItemsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean {
        return oldItem.num == newItem.num
    }

    override fun areContentsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean {
        return oldItem == newItem
    }
}