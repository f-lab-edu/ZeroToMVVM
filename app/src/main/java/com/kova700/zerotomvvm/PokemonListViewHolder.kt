package com.kova700.zerotomvvm

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonListViewHolder(itemView: View, itemClickListener: PokemonItemClickListener) :
    RecyclerView.ViewHolder(itemView) {

    private val carView: CardView =
        itemView.findViewById<CardView?>(R.id.cv_home_item)
            .apply {
                setOnClickListener {
                    itemClickListener.onItemClick(absoluteAdapterPosition)
                }
            }
    private val wishToggleBtn = itemView.findViewById<ToggleButton?>(R.id.tg_wish_home_item)
        .apply {
            setOnClickListener {
                itemClickListener.onHeartClick(absoluteAdapterPosition)
            }
        }
    private val pokemonImageView: ImageView = itemView.findViewById(R.id.iv_pokemon_image_home_item)
    private val pokemonNameTextView: TextView =
        itemView.findViewById(R.id.tv_pokemon_name_home_item)

    fun bind(pokemonListItem: PokemonListItem) {
        Glide.with(itemView.context)
            .load(pokemonListItem.pokemon.getImageUrl())
            .into(pokemonImageView)

        pokemonNameTextView.text = pokemonListItem.pokemon.name
        wishToggleBtn.isChecked = pokemonListItem.heart
    }
}
