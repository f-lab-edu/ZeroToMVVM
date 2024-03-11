package com.kova700.zerotomvvm.ui.main.mapper

import com.kova700.zerotomvvm.domain.model.Pokemon
import com.kova700.zerotomvvm.ui.main.model.PokemonListItem

fun PokemonListItem.toPokemon(): Pokemon {
	return Pokemon(
		num = num,
		name = name,
		imageUrl = imageUrl,
		detailInfoUrl = defaultInfoUrl,
		heart = heart
	)
}

fun Pokemon.toPokemonListItem(): PokemonListItem {
	return PokemonListItem(
		num = num,
		name = name,
		imageUrl = imageUrl,
		defaultInfoUrl = detailInfoUrl,
		heart = heart
	)
}

fun List<Pokemon>.toPokemonListItem() = this.map { it.toPokemonListItem() }
fun List<PokemonListItem>.toPokemon() = this.map { it.toPokemon() }