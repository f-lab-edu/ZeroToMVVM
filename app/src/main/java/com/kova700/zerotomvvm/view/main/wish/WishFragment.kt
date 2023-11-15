package com.kova700.zerotomvvm.view.main.wish

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.kova700.zerotomvvm.R
import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepositoryImpl
import com.kova700.zerotomvvm.databinding.FragmentWishBinding
import com.kova700.zerotomvvm.view.detail.DetailActivity
import com.kova700.zerotomvvm.view.main.MainActivity
import com.kova700.zerotomvvm.view.main.adapter.PokemonListAdapter
import com.kova700.zerotomvvm.view.main.wish.presenter.WishContract
import com.kova700.zerotomvvm.view.main.wish.presenter.WishPresenter

class WishFragment : Fragment(), WishContract.View {

    private var _binding: FragmentWishBinding? = null
    private val binding get() = _binding!!
    private val wishAdapter by lazy { PokemonListAdapter() }

    private val presenter by lazy {
        WishPresenter(
            view = this,
            adapterView = wishAdapter,
            adapterModel = wishAdapter,
            repository = PokemonRepositoryImpl.getInstance(PokemonApi.service)
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
        presenter.loadWishPokemonList()
    }

    override fun onResume() {
        super.onResume()
        presenter.renewPokemonList()
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
                    onRestoreInstanceState(arguments?.getParcelable(WISH_RCV_STATE_KEY))
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rcvState = binding.rcvWishFragment.layoutManager?.onSaveInstanceState()
        _binding = null
    }

    override fun moveToDetail(selectedItem: PokemonListItem) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(MainActivity.TO_DETAIL_SELECTED_ITEM_EXTRA, selectedItem)
        }
        startActivity(intent)
    }

    companion object {
        private const val WISH_RCV_STATE_KEY = "WISH_RCV_STATE_KEY"

        //ViewModel이나 Activity의 생명주기에 따라 삭제되는 곳으로 이전하는 게 좋아 보임 (메모리가 계속 남아있을테니)
        var rcvState: Parcelable? = null

        fun newInstance(): WishFragment {
            return WishFragment().apply {
                arguments = bundleOf(WISH_RCV_STATE_KEY to rcvState)
            }
        }
    }
}