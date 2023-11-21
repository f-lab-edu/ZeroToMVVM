package com.kova700.zerotomvvm.view.main.wish

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kova700.zerotomvvm.R
import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepositoryImpl
import com.kova700.zerotomvvm.databinding.FragmentWishBinding
import com.kova700.zerotomvvm.view.detail.DetailActivity
import com.kova700.zerotomvvm.view.main.MainActivity
import com.kova700.zerotomvvm.view.main.adapter.PokemonListAdapter
import com.kova700.zerotomvvm.view.main.wish.presenter.WishContract
import com.kova700.zerotomvvm.view.main.wish.presenter.WishPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class WishFragment : Fragment(), WishContract.View {

    private var _binding: FragmentWishBinding? = null
    private val binding get() = _binding!!
    override val lifecycleScope: CoroutineScope = lifecycle.coroutineScope
    private val wishAdapter by lazy { PokemonListAdapter() }

    private val presenter by lazy {
        WishPresenter(
            view = this,
            adapterView = wishAdapter,
            adapterModel = wishAdapter,
            repository = PokemonRepositoryImpl.getInstance(
                PokemonApi.service,
                AppDataBase.service
            )
        )
    }

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
        initRecyclerView()
        loadLocalWishPokemonList()
    }

    private fun loadLocalWishPokemonList() = lifecycleScope.launch {
        presenter.loadLocalWishPokemonList()
    }

    override fun onResume() {
        super.onResume()
        loadLocalWishPokemonList()
    }

    private fun initRecyclerView() {
        binding.rcvWishFragment.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun moveToDetail(selectedItem: PokemonListItem) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(MainActivity.TO_DETAIL_SELECTED_ITEM_EXTRA, selectedItem)
        }
        startActivity(intent)
    }
}