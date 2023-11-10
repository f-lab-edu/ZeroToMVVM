package com.kova700.zerotomvvm

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Pokemon(
    @SerialName("name") val name: String,
    @SerialName("url") val detailInfoUrl: String
) : Serializable {
    fun getImageUrl(): String {
        val index = detailInfoUrl.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                "pokemon/other/official-artwork/$index.png"
    }
}

sealed interface PokemonListItemType {
    val pokemon: Pokemon
    val heart: Boolean
}

data class PokemonListItem(
    override val pokemon: Pokemon,
    override val heart: Boolean
) : Serializable, PokemonListItemType

data class EmptyPokemonListItem(
    override val pokemon: Pokemon = Pokemon(" ", " "),
    override val heart: Boolean = false
) : Serializable, PokemonListItemType