package com.kova700.zerotomvvm.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonEntity(
	@PrimaryKey
	val num: Int,
	val name: String,
	val heart: Boolean,
	val detailInfoUrl: String,
	val imageUrl: String,
)