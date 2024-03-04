package com.kova700.zerotomvvm.ui.main.model

data class PokemonListItem(
	val num: Int,
	val name: String,
	val imageUrl: String,
	val defaultInfoUrl: String,
	val heart: Boolean
)