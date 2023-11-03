package com.kova700.zerotomvvm

import android.util.Log
import kotlin.random.Random

fun getDummy(): MutableList<PokemonListItem> {
    Log.d(TAG, "SampleDummy : getDummy() - called")
    val dummyList = mutableListOf<PokemonListItem>()

    (1..100).forEach { i ->
        val name = "Pokemon $i"
        val detailInfoUrl = "https://pokeapi.co/api/v2/pokemon/$i/"
        val isHeart = Random.nextInt() % 2 == 0

        val pokemon = Pokemon(name, detailInfoUrl)
        val pokemonListItem = PokemonListItem(pokemon, isHeart)

        dummyList.add(pokemonListItem)
    }

    return dummyList
}

fun getRandomDummyItem(startNum: Int): PokemonListItem {
    val name = "Pokemon $startNum"
    val detailInfoUrl = "https://pokeapi.co/api/v2/pokemon/$startNum/"
    val isHeart = Random.nextInt() % 2 == 0

    val pokemon = Pokemon(name, detailInfoUrl)
    return PokemonListItem(pokemon, isHeart)
}