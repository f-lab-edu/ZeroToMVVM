package com.kova700.zerotomvvm.view.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.databinding.ActivityDetailBinding
import com.kova700.zerotomvvm.util.getIntExtraData
import com.kova700.zerotomvvm.util.getSerializableExtraData
import com.kova700.zerotomvvm.view.main.MainActivity.Companion.TO_DETAIL_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.view.main.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_EXTRA

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var pokemonListItem: PokemonListItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPokemonExtraData()
        setBackPressedEvent()
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
            setOnClickListener {
                pokemonListItem = pokemonListItem.copy(heart = pokemonListItem.heart.not())
            }
        }
    }

    private fun setBackPressedEvent() {
        onBackPressedDispatcher.addCallback {
            finishActivity()
        }
    }

    private fun finishActivity() {
        val resultIntent = Intent().apply {
            putExtra(TO_MAIN_HEART_BOOLEAN_EXTRA, pokemonListItem.heart)
            putExtra(
                TO_MAIN_ITEM_POSITION_EXTRA,
                intent.getIntExtraData(TO_DETAIL_ITEM_POSITION_EXTRA)
            )

        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        const val TO_MAIN_HEART_BOOLEAN_EXTRA = "TO_MAIN_EXTRA"
        const val TO_MAIN_ITEM_POSITION_EXTRA = "TO_MAIN_ITEM_POSITION_EXTRA"
    }
}