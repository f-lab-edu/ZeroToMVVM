package com.kova700.zerotomvvm.ui.main.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.kova700.zerotomvvm.ui.main.model.PokemonListItem
import com.kova700.zerotomvvm.databinding.ItemPokemonListBinding

class PokemonListViewHolder(
    private val binding: ItemPokemonListBinding,
    private val itemClickListener: ((Int) -> Unit)?,
    private var heartClickListener: ((Int) -> Unit)?,
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
        val circularProgressDrawable = CircularProgressDrawable(binding.root.context)
            .apply {
                strokeWidth = 5f
                centerRadius = 30f
                start()
            }

        Glide.with(itemView.context)
            .load(pokemonListItem.imageUrl)
            .placeholder(circularProgressDrawable)
            .into(binding.ivPokemonImageHomeItem)

        binding.tvPokemonNameHomeItem.text = pokemonListItem.name
        binding.tgWishHomeItem.isChecked = pokemonListItem.heart
    }
}
