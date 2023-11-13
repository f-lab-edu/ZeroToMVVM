package com.kova700.zerotomvvm.view.main.home

import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.kova700.zerotomvvm.R
import com.kova700.zerotomvvm.databinding.FragmentHomeBinding
import com.kova700.zerotomvvm.util.getBooleanExtraData
import com.kova700.zerotomvvm.util.getIntExtraData
import com.kova700.zerotomvvm.view.detail.DetailActivity.Companion.TO_MAIN_HEART_BOOLEAN_EXTRA
import com.kova700.zerotomvvm.view.detail.DetailActivity.Companion.TO_MAIN_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.view.main.MainActivity
import com.kova700.zerotomvvm.view.main.adapter.PokemonListAdapter
import com.kova700.zerotomvvm.view.main.home.presenter.HomeContract
import com.kova700.zerotomvvm.view.main.home.presenter.HomePresenter

class HomeFragment : Fragment(), HomeContract.View {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeAdapter: PokemonListAdapter by lazy { PokemonListAdapter() }
    private lateinit var activity: LifecycleOwner

    private val presenter by lazy {
        HomePresenter(
            view = this@HomeFragment,
            adapterView = homeAdapter,
            adapterModel = homeAdapter,
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as? MainActivity ?: throw Exception("Unknown Activity")
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
        presenter.observePokemonList()
    }

    private fun setPlusBtnClickListener() {
        binding.fabHomeFragment.setOnClickListener { presenter.addRandomItem() }
    }

    val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult

            val intent = result.data ?: throw Exception("DetailActivity result is not exist")
            val heartValue = intent.getBooleanExtraData(TO_MAIN_HEART_BOOLEAN_EXTRA)
            val itemPosition = intent.getIntExtraData(TO_MAIN_ITEM_POSITION_EXTRA)

            val newList = homeAdapter.currentList.toMutableList().apply {
                this[itemPosition] = this[itemPosition].copy(heart = heartValue)
            }
            presenter.updatePokemonList(newList)
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