package com.kova700.zerotomvvm

import kotlin.random.Random

fun getDummy(): MutableList<PokemonListItem> {
    val dummyList = mutableListOf<PokemonListItem>()

    (1..20).forEach { i ->
        val name = "Pokemon $i"
        val detailInfoUrl = "https://pokeapi.co/api/v2/pokemon/$i/"
        val isHeart = Random.nextInt() % 2 == 0

        val pokemon = Pokemon(name, detailInfoUrl)
        val pokemonListItem = PokemonListItem(pokemon, isHeart)

        dummyList.add(pokemonListItem)
    }

    return dummyList
}