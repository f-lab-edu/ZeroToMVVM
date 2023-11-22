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
import com.kova700.zerotomvvm.view.main.PokemonViewModel
import com.kova700.zerotomvvm.view.main.PokemonViewModel.MoveToDetail
import com.kova700.zerotomvvm.view.main.PokemonViewModel.PokemonUiEvent
import com.kova700.zerotomvvm.view.main.PokemonViewModel.ShowToast
import com.kova700.zerotomvvm.view.main.adapter.PokemonListAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var isFirstResume = true
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val pokemonViewModel by activityViewModels<PokemonViewModel>()
    private val homeAdapter: PokemonListAdapter by lazy {
        PokemonListAdapter(
            onItemClick = { itemPosition ->
                pokemonViewModel.itemClickListener(homeAdapter.currentList[itemPosition])
            },
            onHeartClick = { itemPosition ->
                pokemonViewModel.homeHeartClickListener(homeAdapter.currentList[itemPosition])
            }
        )
    }

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
        setPlusBtnClickListener()
        observeUiEvent()
        observeLoadingFlag()
        observePokemonListFlow()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstResume) {
            isFirstResume = false
            return
        }
        renewPokemonList()
    }

    private fun renewPokemonList() = viewLifecycleOwner.lifecycleScope.launch {
        pokemonViewModel.renewPokemonList()
    }

    private fun observeUiEvent() = viewLifecycleOwner.lifecycleScope.launch {
        pokemonViewModel.eventFlow.collect { event ->
            handleUiEvent(event)
        }
    }

    private fun observeLoadingFlag() = viewLifecycleOwner.lifecycleScope.launch {
        pokemonViewModel.isLoading.collect { isLoading ->
            handleLoading(isLoading)
        }
    }

    private fun observePokemonListFlow() = viewLifecycleOwner.lifecycleScope.launch {
        pokemonViewModel.pokemonListFlow.collect { pokemonList ->
            homeAdapter.submitList(pokemonList)
        }
    }

    private fun setPlusBtnClickListener() {
        binding.fabHomeFragment.setOnClickListener { pokemonViewModel.plusBtnClickListener() }
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
                pokemonViewModel.loadNextPokemonList(
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun handleLoading(isLoading: Boolean) {
        _binding?.pbHomeFragment?.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }
}