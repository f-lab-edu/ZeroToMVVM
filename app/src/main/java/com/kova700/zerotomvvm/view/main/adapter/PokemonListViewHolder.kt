package com.kova700.zerotomvvm.view.main.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.databinding.ItemPokemonListBinding

class PokemonListViewHolder(
    private val binding: ItemPokemonListBinding,
    private val itemClickListener : ((Int) -> Unit)?,
    private var heartClickListener : ((Int) -> Unit)?,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.cvHomeItem.setOnClickListener {
            itemClickListener?.invoke(absoluteAdapterPosition)
        }
        binding.tgWishHomeItem.setOnClickListener {
            heartClickListener?.invoke(absoluteAdapterPosition)
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
