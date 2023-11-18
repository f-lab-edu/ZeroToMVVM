package com.kova700.zerotomvvm.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.bumptech.glide.Glide
import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepositoryImpl
import com.kova700.zerotomvvm.databinding.ActivityDetailBinding
import com.kova700.zerotomvvm.util.getSerializableExtraData
import com.kova700.zerotomvvm.view.detail.presenter.DetailContract
import com.kova700.zerotomvvm.view.detail.presenter.DetailPresenter
import com.kova700.zerotomvvm.view.main.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_EXTRA
import kotlinx.coroutines.CoroutineScope

class DetailActivity : AppCompatActivity(), DetailContract.View {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var pokemonListItem: PokemonListItem
    override val lifecycleScope: CoroutineScope = lifecycle.coroutineScope

    private val presenter by lazy {
        DetailPresenter(
            view = this,
            repository = PokemonRepositoryImpl.getInstance(
                PokemonApi.service,
                AppDataBase.service
            )
        )
    }

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
            setOnClickListener { presenter.heartClickListener(pokemonListItem) }
        }
    }
}