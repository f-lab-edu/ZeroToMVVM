package com.kova700.zerotomvvm

import java.io.Serializable

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

// TODO: heart 수정 불가 타입으로 수정
sealed interface PokemonListItemType {
    val pokemon: Pokemon
    var heart: Boolean
}

data class PokemonListItem(
    override val pokemon: Pokemon,
    override var heart: Boolean
) : Serializable, PokemonListItemType

data class EmptyPokemonListItem(
    override val pokemon: Pokemon = Pokemon(" ", " "),
    override var heart: Boolean = false
) : Serializable, PokemonListItemType