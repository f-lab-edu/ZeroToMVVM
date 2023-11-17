package com.kova700.zerotomvvm.data.source.pokemon.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kova700.zerotomvvm.data.source.pokemon.Pokemon
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem

@Entity
data class PokemonEntity(
    @PrimaryKey
    val num: Int,
    val name: String,
    val heart: Boolean,
    val detailInfoUrl: String
) {
    fun toListItem() = PokemonListItem(
        Pokemon(name, detailInfoUrl),
        heart
    )
}