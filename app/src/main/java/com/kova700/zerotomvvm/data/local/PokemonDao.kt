package com.kova700.zerotomvvm.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kova700.zerotomvvm.data.local.model.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertPokemon(pokemon: PokemonEntity)

	@Query("DELETE FROM PokemonEntity where num = :num")
	suspend fun deletePokemon(num: Int)

	@Query(
		"SELECT * FROM PokemonEntity " +
						"WHERE heart = :heartValue "
	)
	fun getWishPokemons(heartValue: Boolean = true): Flow<List<PokemonEntity>>
}