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

    private val pokemonListItem: PokemonListItemType by lazy {
        intent.getSerializableExtraData(TO_DETAIL_SELECTED_ITEM_EXTRA, PokemonListItem::class.java)
            ?: EmptyPokemonListItem().also {
                showSnackBar(
                    window.decorView.rootView,
                    binding.glBottomDetailActivity,
                    R.string.to_detail_activity_data_empty
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBackPressedEvent()
        initUIComponent()
    }

    private fun initUIComponent() {
        if (pokemonListItem is EmptyPokemonListItem) return

        Glide.with(this)
            .load(pokemonListItem.pokemon.getImageUrl())
            .into(binding.ivPokemonImageDetailActivity)

        binding.tgDetailActivity.apply {
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
        if (pokemonListItem is EmptyPokemonListItem) {
            finish()
            return
        }
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
