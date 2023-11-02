package com.kova700.zerotomvvm

import java.io.Serializable

data class PokemonListItem(
    val pokemon: Pokemon,
    val heart: Boolean
) : Serializable

data class Pokemon(
    val name: String,
    val detailInfoUrl: String
) : Serializable {
    fun getImageUrl(): String {
        val index = detailInfoUrl.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                "pokemon/other/official-artwork/$index.png"
    }
}