package com.kova700.zerotomvvm

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_EXTRA

class DetailActivity : AppCompatActivity(R.layout.activity_detail) {

    private val pokemonListItem by lazy {
        intent.getSerializableExtraData(TO_DETAIL_SELECTED_ITEM_EXTRA, PokemonListItem::class.java)
            ?: throw Exception("pokemon is not exist")
    }

    private val pokemonImageView: ImageView by lazy {
        findViewById(R.id.iv_pokemon_image_detail_activity)
    }

    private val heartToggleButton: ToggleButton by lazy {
        findViewById(R.id.tg_detail_activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackPressedEvent()
        initUIComponent()
    }

    private fun initUIComponent() {
        Glide.with(this)
            .load(pokemonListItem.pokemon.getImageUrl())
            .into(pokemonImageView)

        heartToggleButton.apply {
            isChecked = pokemonListItem.heart
            setOnClickListener {
                pokemonListItem.heart = !pokemonListItem.heart
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
        intent.putExtra(TO_MAIN_ITEM_POSITION_EXTRA, intent.getIntExtraData(TO_DETAIL_ITEM_POSITION_EXTRA))
        finishWithExtra()
    }

    companion object {
        const val TO_MAIN_HEART_BOOLEAN_EXTRA = "TO_MAIN_EXTRA"
        const val TO_MAIN_ITEM_POSITION_EXTRA = "TO_MAIN_ITEM_POSITION_EXTRA"
    }
}
