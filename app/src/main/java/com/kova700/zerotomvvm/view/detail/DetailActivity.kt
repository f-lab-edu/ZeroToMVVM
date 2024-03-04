package com.kova700.zerotomvvm.view.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kova700.zerotomvvm.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUIComponent()
    }

    private fun initUIComponent() {
        Glide.with(this)
            .load(detailViewModel.selectedItem.pokemon.getImageUrl())
            .into(binding.ivPokemonImageDetailActivity)

        binding.tgDetailActivity.apply {
            isChecked = detailViewModel.selectedItem.heart
            setOnClickListener { detailViewModel.onHeartClick() }
        }
    }
}