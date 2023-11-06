package com.kova700.zerotomvvm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.kova700.zerotomvvm.DetailActivity.Companion.TO_MAIN_HEART_BOOLEAN_EXTRA
import com.kova700.zerotomvvm.DetailActivity.Companion.TO_MAIN_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_EXTRA
import com.kova700.zerotomvvm.databinding.FragmentWishBinding

class WishFragment : Fragment() {

    private var _binding: FragmentWishBinding? = null
    private val binding get() = _binding!!
    lateinit var wishAdapter: PokemonListAdapter
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as? MainActivity ?: throw Exception("Unknown Activity")
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
        initAdapter()
        initRecyclerView()
        inflateWishData()
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult

            val intent = result.data ?: throw Exception("DetailActivity result is not exist")
            val heartValue = intent.getBooleanExtraData(TO_MAIN_HEART_BOOLEAN_EXTRA)
            val itemPosition = intent.getIntExtraData(TO_MAIN_ITEM_POSITION_EXTRA)
            if (heartValue) return@registerForActivityResult
            removeWishItem(wishAdapter.currentList[itemPosition])
        }

    private fun initAdapter() {
        val itemClickListener = object : PokemonItemClickListener {
            override fun onItemClick(itemPosition: Int) {
                val selectedItem = wishAdapter.currentList[itemPosition]
                val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                    putExtra(TO_DETAIL_SELECTED_ITEM_EXTRA, selectedItem)
                    putExtra(TO_DETAIL_ITEM_POSITION_EXTRA, itemPosition)
                }
                activityResultLauncher.launch(intent)
            }

            override fun onHeartClick(itemPosition: Int) {
                val selectedItem = wishAdapter.currentList[itemPosition]
                if (selectedItem.heart) removeWishItem(selectedItem)
            }
        }
        wishAdapter = PokemonListAdapter(itemClickListener)
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

    private fun removeWishItem(selectedItem: PokemonListItem) {
        mainActivity.homePokeymonList.forEachIndexed { index, pokemonListItem ->
            if (pokemonListItem.pokemon.name != selectedItem.pokemon.name) return@forEachIndexed
            mainActivity.homePokeymonList[index] = selectedItem.copy(heart = false)
        }
        wishAdapter.submitList(mainActivity.wishPokeymonList.toList())
    }

    private fun inflateWishData() {
        wishAdapter.submitList(mainActivity.wishPokeymonList.toList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rcvState = binding.rcvWishFragment.layoutManager?.onSaveInstanceState()
        _binding = null
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