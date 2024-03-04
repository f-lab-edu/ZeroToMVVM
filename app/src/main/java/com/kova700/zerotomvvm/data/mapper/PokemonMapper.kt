package com.kova700.zerotomvvm.data.mapper

import com.kova700.zerotomvvm.data.local.model.PokemonEntity
import com.kova700.zerotomvvm.data.remote.model.PokemonResponse
import com.kova700.zerotomvvm.domain.model.Pokemon

fun Pokemon.toPokemonEntity(): PokemonEntity {
	return PokemonEntity(
		num = num,
		name = name,
		heart = true,
		imageUrl = imageUrl,
		detailInfoUrl = detailInfoUrl
	)
}

fun PokemonResponse.toPokemon(): Pokemon {
	return Pokemon(
		num = getPokemonNum(),
		name = name,
		imageUrl = getImageUrl(),
		detailInfoUrl = detailInfoUrl,
		heart = false,
	)
}

fun PokemonEntity.toPokemon(): Pokemon {
	return Pokemon(
		num = num,
		name = name,
		imageUrl = imageUrl,
		detailInfoUrl = detailInfoUrl,
		heart = heart,
	)
}

fun List<PokemonEntity>.toPokemonFromEntity() = this.map { it.toPokemon() }
fun List<PokemonResponse>.toPokemonFromResponse() = this.map { it.toPokemon() }