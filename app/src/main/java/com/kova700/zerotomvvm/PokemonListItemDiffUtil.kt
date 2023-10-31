package com.kova700.zerotomvvm

import androidx.recyclerview.widget.DiffUtil

class PokemonListItemDiffUtil : DiffUtil.ItemCallback<PokemonListItem>() {
    override fun areItemsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean {
        return (oldItem.pokemon.name == newItem.pokemon.name) && (oldItem.heart == newItem.heart)
    }

    override fun areContentsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean {
        return oldItem == newItem
    }
}