package com.kova700.zerotomvvm.view.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepositoryImpl
import com.kova700.zerotomvvm.databinding.ActivityDetailBinding
import com.kova700.zerotomvvm.util.getSerializableExtraData
import com.kova700.zerotomvvm.view.main.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_EXTRA

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        DetailViewModel.provideFactory(
            PokemonRepositoryImpl.getInstance(
                PokemonApi.service,
                AppDataBase.service
            ),
            getPokemonExtraData()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUIComponent()
    }

    private fun getPokemonExtraData(): PokemonListItem {
        return intent.getSerializableExtraData(
            TO_DETAIL_SELECTED_ITEM_EXTRA,
            PokemonListItem::class.java
        )!!
    }

    private fun initUIComponent() {
        Glide.with(this)
            .load(detailViewModel.selectedItem.pokemon.getImageUrl())
            .into(binding.ivPokemonImageDetailActivity)

        binding.tgDetailActivity.apply {
            isChecked = detailViewModel.selectedItem.heart
            setOnClickListener { detailViewModel.heartClickListener() }
        }
    }
}