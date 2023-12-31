package com.kova700.zerotomvvm.data.source.pokemon.local

import android.util.Log
import com.kova700.zerotomvvm.data.source.pokemon.Pokemon
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.util.TAG
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

fun getRandomDummyEntity(startNum: Int): PokemonEntity {
    val name = "Pokemon $startNum"
    val detailInfoUrl = "https://pokeapi.co/api/v2/pokemon/$startNum/"
    val isHeart = Random.nextInt() % 2 == 0

    return PokemonEntity(
        num = startNum,
        name = name,
        heart = isHeart,
        detailInfoUrl = detailInfoUrl,
    )
}