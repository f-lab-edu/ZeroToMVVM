package com.kova700.zerotomvvm.view.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kova700.zerotomvvm.R
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.databinding.FragmentHomeBinding
import com.kova700.zerotomvvm.util.showToast
import com.kova700.zerotomvvm.view.detail.DetailActivity
import com.kova700.zerotomvvm.view.main.MainActivity
import com.kova700.zerotomvvm.view.main.MainUiState
import com.kova700.zerotomvvm.view.main.MainViewModel
import com.kova700.zerotomvvm.view.main.MoveToDetail
import com.kova700.zerotomvvm.view.main.PokemonUiEvent
import com.kova700.zerotomvvm.view.main.ShowToast
import com.kova700.zerotomvvm.view.main.adapter.PokemonListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!
	private val mainViewModel by activityViewModels<MainViewModel>()

	@Inject
	lateinit var homeAdapter: PokemonListAdapter

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentHomeBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		observeUiState()
		observeUiEvent()
		initAdapter()
		initRecyclerView()
		setRetryBtnClickListener()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


	private fun observeUiState() = viewLifecycleOwner.lifecycleScope.launch {
		mainViewModel.uiState.collect { uistate ->
			homeAdapter.submitList(uistate.pokemons)
			handleLoading(uistate.uiState)
			handelRetryBtn(uistate.uiState)
		}
	}

	private fun observeUiEvent() = viewLifecycleOwner.lifecycleScope.launch {
		mainViewModel.eventFlow.collect { event ->
			handleUiEvent(event)
		}
	}

	private fun setRetryBtnClickListener() {
		binding.fabHomeFragment.setOnClickListener { mainViewModel.onRetryClick() }
	}

	private fun initAdapter() {
		homeAdapter.apply {
			onItemClick = { itemPosition ->
				mainViewModel.onItemClick(homeAdapter.currentList[itemPosition])
			}
			onHeartClick = { itemPosition ->
				mainViewModel.onHeartClick(homeAdapter.currentList[itemPosition])
			}
		}
	}

	private fun initRecyclerView() {
		val gridLayoutManager = GridLayoutManager(requireActivity(), 2).apply {
			spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
				override fun getSpanSize(position: Int): Int {
					return when (homeAdapter.getItemViewType(position)) {
						R.layout.item_pokemon_list -> 1
						else -> throw Exception("Unknown Item Layout")
					}
				}
			}
		}

		val rcvScrollListener = object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
				mainViewModel.loadNextPokemons(
					gridLayoutManager.findLastVisibleItemPosition()
				)
			}
		}

		binding.rcvHomeFragment.apply {
			adapter = homeAdapter
			layoutManager = gridLayoutManager
			addOnScrollListener(rcvScrollListener)
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

	private fun handleLoading(uiState: MainUiState.UiState) {
		val isLoading = uiState == MainUiState.UiState.LOADING
		_binding?.pbHomeFragment?.visibility =
			if (isLoading) View.VISIBLE else View.GONE
	}

	private fun handelRetryBtn(uiState: MainUiState.UiState) {
		val isError = uiState == MainUiState.UiState.ERROR
		binding.fabHomeFragment.visibility = if (isError) View.VISIBLE else View.GONE
	}
}