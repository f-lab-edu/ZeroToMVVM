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

data class PokemonListItem(
    val pokemon: Pokemon,
    val heart: Boolean
) : Serializable