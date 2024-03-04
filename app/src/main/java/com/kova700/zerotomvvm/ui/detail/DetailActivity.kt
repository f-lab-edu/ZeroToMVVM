package com.kova700.zerotomvvm.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.kova700.zerotomvvm.databinding.ActivityDetailBinding
import com.kova700.zerotomvvm.domain.model.Pokemon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
	private lateinit var binding: ActivityDetailBinding

	private val detailViewModel by viewModels<DetailViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDetailBinding.inflate(layoutInflater)
		setContentView(binding.root)
		initUIComponent()
		observeUiState()
	}

	private fun observeUiState() = lifecycleScope.launch {
		detailViewModel.uiState.collect { pokemon ->
			setCheckToggleBtn(pokemon)
		}
	}

	private fun setCheckToggleBtn(pokemon: Pokemon) {
		binding.tgDetailActivity.isChecked = pokemon.heart
	}

	private fun initUIComponent() {
		Glide.with(this)
			.load(detailViewModel.uiState.value.imageUrl)
			.into(binding.ivPokemonImageDetailActivity)

		binding.tgDetailActivity.apply {
			isChecked = detailViewModel.uiState.value.heart
			setOnClickListener { detailViewModel.onHeartClick() }
		}
	}
}