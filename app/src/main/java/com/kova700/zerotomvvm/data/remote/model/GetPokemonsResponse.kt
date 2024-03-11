package com.kova700.zerotomvvm.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPokemonsResponse(
	@SerialName("count") val count: Int,
	@SerialName("next") val next: String? = null,
	@SerialName("previous") val previous: String? = null,
	@SerialName("results") val results: List<PokemonResponse>
)