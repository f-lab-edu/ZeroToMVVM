package com.kova700.zerotomvvm.view.main.wish

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kova700.zerotomvvm.R
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.databinding.FragmentWishBinding
import com.kova700.zerotomvvm.util.showToast
import com.kova700.zerotomvvm.view.detail.DetailActivity
import com.kova700.zerotomvvm.view.main.MainActivity
import com.kova700.zerotomvvm.view.main.MainViewModel
import com.kova700.zerotomvvm.view.main.MoveToDetail
import com.kova700.zerotomvvm.view.main.PokemonUiEvent
import com.kova700.zerotomvvm.view.main.ShowToast
import com.kova700.zerotomvvm.view.main.adapter.PokemonListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WishFragment : Fragment() {

	private var _binding: FragmentWishBinding? = null
	private val binding get() = _binding!!
	private val mainViewModel by activityViewModels<MainViewModel>()

	@Inject
	lateinit var wishAdapter: PokemonListAdapter

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentWishBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		observeUiState()
		observeUiEvent()
		initAdapter()
		initRecyclerView()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun observeUiState() = viewLifecycleOwner.lifecycleScope.launch {
		mainViewModel.uiState.collect { uistate ->
			wishAdapter.submitList(uistate.wishPokemons)
		}
	}

	private fun observeUiEvent() = viewLifecycleOwner.lifecycleScope.launch {
		mainViewModel.eventFlow.collect { event ->
			handleUiEvent(event)
		}
	}

	private fun initAdapter() {
		wishAdapter.apply {
			onItemClick = { itemPosition ->
				mainViewModel.onItemClick(wishAdapter.currentList[itemPosition])
			}
			onHeartClick = { itemPosition ->
				mainViewModel.onHeartClick(wishAdapter.currentList[itemPosition])
			}
		}
	}

	private fun initRecyclerView() {
		with(binding.rcvWishFragment) {
			adapter = wishAdapter
			layoutManager = GridLayoutManager(requireActivity(), 2)
				.apply {
					spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
						override fun getSpanSize(position: Int): Int {
							return when (wishAdapter.getItemViewType(position)) {
								R.layout.item_pokemon_list -> 1
								else -> throw Exception("Unknown Item Layout")
							}
						}
					}
				}
		}
	}

	private fun moveToDetail(selectedItem: PokemonListItem) {
		val intent = Intent(activity, DetailActivity::class.java).apply {
			putExtra(MainActivity.TO_DETAIL_SELECTED_ITEM_EXTRA, selectedItem)
		}
		startActivity(intent)
	}

	private fun showToast(message: String) {
		requireActivity().showToast(message)
	}

	private fun handleUiEvent(event: PokemonUiEvent) {
		when (event) {
			is MoveToDetail -> moveToDetail(event.selectedItem)
			is ShowToast -> showToast(event.message)
		}
	}
}