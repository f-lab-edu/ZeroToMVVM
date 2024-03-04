package com.kova700.zerotomvvm.data.source.pokemon

import com.kova700.zerotomvvm.data.source.pokemon.local.PokemonEntity
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

	fun toDBEntity() = PokemonEntity(
		num = getPokemonNum(),
		name = this.name,
		heart = true,
		detailInfoUrl = this.detailInfoUrl,
	)
}

data class PokemonListItem(
	val pokemon: Pokemon,
	val heart: Boolean = false
) : Serializable

fun Pokemon.toPokemonListItem(): PokemonListItem {
	return PokemonListItem(pokemon = this)
}

fun PokemonEntity.toPokemonListItem(): PokemonListItem {
	return PokemonListItem(
		pokemon = Pokemon(
			name = name,
			detailInfoUrl = detailInfoUrl
		),
		heart = heart
	)
}
fun List<PokemonEntity>.toListItem() = this.map { it.toListItem() }
fun List<Pokemon>.toDBEntity() = this.map { it.toDBEntity() }