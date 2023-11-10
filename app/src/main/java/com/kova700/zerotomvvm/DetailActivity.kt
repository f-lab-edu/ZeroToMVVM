package com.kova700.zerotomvvm

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_EXTRA
import com.kova700.zerotomvvm.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(R.layout.activity_detail) {
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
        intent.putExtra(TO_MAIN_HEART_BOOLEAN_EXTRA, pokemonListItem.heart)
        intent.putExtra(
            TO_MAIN_ITEM_POSITION_EXTRA,
            intent.getIntExtraData(TO_DETAIL_ITEM_POSITION_EXTRA)
        )
        finishWithExtra()
    }

    companion object {
        const val TO_MAIN_HEART_BOOLEAN_EXTRA = "TO_MAIN_EXTRA"
        const val TO_MAIN_ITEM_POSITION_EXTRA = "TO_MAIN_ITEM_POSITION_EXTRA"
    }
}
