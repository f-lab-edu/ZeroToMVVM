package com.kova700.zerotomvvm.view.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

class PokemonListItemDiffUtil : DiffUtil.ItemCallback<PokemonListItem>() {
    override fun areItemsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean {
        return oldItem.pokemon.name == newItem.pokemon.name
    }

    override fun areContentsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean {
        return oldItem == newItem
    }
}