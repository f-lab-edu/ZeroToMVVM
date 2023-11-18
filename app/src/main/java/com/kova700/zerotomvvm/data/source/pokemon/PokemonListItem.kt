package com.kova700.zerotomvvm.data.source.pokemon

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Pokemon(
    @SerialName("name") val name: String,
    @SerialName("url") val detailInfoUrl: String
) : Serializable {
    fun getPokemonNum() = detailInfoUrl.split("/".toRegex()).dropLast(1).last().toInt()
    fun getImageUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                "pokemon/other/official-artwork/${getPokemonNum()}.png"
    }
}

data class PokemonListItem(
    val pokemon: Pokemon,
    val heart: Boolean = false
) : Serializable