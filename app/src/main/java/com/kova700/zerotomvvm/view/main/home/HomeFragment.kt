package com.kova700.zerotomvvm.view.main.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kova700.zerotomvvm.R
import com.kova700.zerotomvvm.data.api.PokemonApi
import com.kova700.zerotomvvm.data.source.pokemon.PokemonListItem
import com.kova700.zerotomvvm.data.source.pokemon.remote.PokemonRepositoryImpl
import com.kova700.zerotomvvm.databinding.FragmentHomeBinding
import com.kova700.zerotomvvm.util.getBooleanExtraData
import com.kova700.zerotomvvm.util.getIntExtraData
import com.kova700.zerotomvvm.view.detail.DetailActivity
import com.kova700.zerotomvvm.view.detail.DetailActivity.Companion.TO_MAIN_HEART_BOOLEAN_EXTRA
import com.kova700.zerotomvvm.view.detail.DetailActivity.Companion.TO_MAIN_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.view.main.MainActivity
import com.kova700.zerotomvvm.view.main.adapter.PokemonListAdapter
import com.kova700.zerotomvvm.view.main.home.presenter.HomeContract
import com.kova700.zerotomvvm.view.main.home.presenter.HomePresenter
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), HomeContract.View {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeAdapter: PokemonListAdapter by lazy { PokemonListAdapter() }

    private val presenter by lazy {
        HomePresenter(
            view = this@HomeFragment,
            adapterView = homeAdapter,
            adapterModel = homeAdapter,
            repository = PokemonRepositoryImpl.getInstance(PokemonApi.service)
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
        loadPokemonList()
    }

    private fun loadPokemonList() {
        viewLifecycleOwner.lifecycleScope.launch {
            presenter.loadPokemonList()
        }
    }

    private fun setPlusBtnClickListener() {
        binding.fabHomeFragment.setOnClickListener { presenter.addRandomItem() }
    }

    //TODO : 굳이 이걸로 데이터를 주고 받을 필요가 있을까? (바로 Repository 반영해보자)
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult

            val intent = result.data ?: throw Exception("DetailActivity result is not exist")
            val heartValue = intent.getBooleanExtraData(TO_MAIN_HEART_BOOLEAN_EXTRA)
            val itemPosition = intent.getIntExtraData(TO_MAIN_ITEM_POSITION_EXTRA)
            presenter.updateHeartInPosition(itemPosition, heartValue)
        }

    private fun initRecyclerView() {
        binding.rcvHomeFragment.apply {
            adapter = homeAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (homeAdapter.getItemViewType(position)) {
                            R.layout.item_pokemon_list -> 1
                            else -> throw Exception("Unknown Item Layout")
                        }
                    }
                }
                onRestoreInstanceState(arguments?.getParcelable(HOME_RCV_STATE_KEY))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rcvState = binding.rcvHomeFragment.layoutManager?.onSaveInstanceState()
        _binding = null //쓰지 않을 bindingView를 가지고 있을 이유가 없음으로 메모리 반환
    }


    override fun moveToDetail(itemPosition: Int, selectedItem: PokemonListItem) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(MainActivity.TO_DETAIL_SELECTED_ITEM_EXTRA, selectedItem)
            putExtra(MainActivity.TO_DETAIL_ITEM_POSITION_EXTRA, itemPosition)
        }
        activityResultLauncher.launch(intent)
    }

    override fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val HOME_RCV_STATE_KEY = "HOME_RCV_STATE_KEY"

        //ViewModel이나 Activity의 생명주기에 따라 삭제되는 곳으로 이전하는 게 좋아 보임 (메모리가 계속 남아있을테니)
        var rcvState: Parcelable? = null

        fun newInstance(): HomeFragment {
            return HomeFragment().apply {
                arguments = bundleOf(HOME_RCV_STATE_KEY to rcvState)
            }
        }
    }
}