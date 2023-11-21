package com.kova700.zerotomvvm.view.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kova700.zerotomvvm.R
import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.db.AppDataBase
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepositoryImpl
import com.kova700.zerotomvvm.databinding.FragmentHomeBinding
import com.kova700.zerotomvvm.view.detail.DetailActivity
import com.kova700.zerotomvvm.view.main.MainActivity
import com.kova700.zerotomvvm.view.main.adapter.PokemonListAdapter
import com.kova700.zerotomvvm.view.main.home.presenter.HomeContract
import com.kova700.zerotomvvm.view.main.home.presenter.HomePresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), HomeContract.View {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override val lifecycleScope: CoroutineScope = lifecycle.coroutineScope
    private val homeAdapter: PokemonListAdapter by lazy { PokemonListAdapter() }

    private val presenter by lazy {
        HomePresenter(
            view = this,
            adapterView = homeAdapter,
            adapterModel = homeAdapter,
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPlusBtnClickListener()
        initRecyclerView()
        loadRemotePokemonList()
    }

    override fun onResume() {
        super.onResume()
        renewPokemonList()
    }

    private fun loadRemotePokemonList() = lifecycleScope.launch {
        presenter.loadRemotePokemonList()
    }

    private fun renewPokemonList() = lifecycleScope.launch {
        presenter.renewPokemonList()
    }

    private fun setPlusBtnClickListener() {
        binding.fabHomeFragment.setOnClickListener { presenter.plusBtnClickListener() }
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
                presenter.loadNextPokemonList(
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

    override fun moveToDetail(selectedItem: PokemonListItem) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(MainActivity.TO_DETAIL_SELECTED_ITEM_EXTRA, selectedItem)
        }
        startActivity(intent)
    }

    override fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        _binding?.pbHomeFragment?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        _binding?.pbHomeFragment?.visibility = View.GONE
    }
}