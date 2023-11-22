package com.kova700.zerotomvvm.view.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.databinding.ActivityDetailBinding
import com.kova700.zerotomvvm.util.getSerializableExtraData
import com.kova700.zerotomvvm.view.main.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_EXTRA

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var pokemonListItem: PokemonListItem
    private val pokemonViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPokemonExtraData()
        initUIComponent()
    }

    private fun getPokemonExtraData() {
        pokemonListItem = intent.getSerializableExtraData(
            TO_DETAIL_SELECTED_ITEM_EXTRA,
            PokemonListItem::class.java
        )!!
    }

    private fun initUIComponent() {
        Glide.with(this)
            .load(pokemonListItem.pokemon.getImageUrl())
            .into(binding.ivPokemonImageDetailActivity)

        binding.tgDetailActivity.apply {
            isChecked = pokemonListItem.heart
            setOnClickListener { pokemonViewModel.heartClickListener(pokemonListItem) }
        }
    }
}