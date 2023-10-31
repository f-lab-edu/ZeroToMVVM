package com.kova700.zerotomvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class PokemonListAdapter : ListAdapter<PokemonListItem, PokemonListViewHolder>(PokemonListItemDiffUtil()) {
    lateinit var itemClickListener: PokemonItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return PokemonListViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_home
}