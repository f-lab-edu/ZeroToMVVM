package com.kova700.zerotomvvm.view.main.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.databinding.ItemPokemonListBinding

class PokemonListViewHolder(
    private val binding: ItemPokemonListBinding,
    itemClickListener: PokemonItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            itemClickListener.onItemClick(absoluteAdapterPosition)
        }
        binding.tgWishHomeItem.setOnClickListener {
            itemClickListener.onHeartClick(absoluteAdapterPosition)
        }
    }

    fun bind(pokemonListItem: PokemonListItem) {
        Glide.with(itemView.context)
            .load(pokemonListItem.pokemon.getImageUrl())
            .into(binding.ivPokemonImageHomeItem)

        binding.tvPokemonNameHomeItem.text = pokemonListItem.pokemon.name
        binding.tgWishHomeItem.isChecked = pokemonListItem.heart
    }
}
