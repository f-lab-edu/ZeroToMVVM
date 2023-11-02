package com.kova700.zerotomvvm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_EXTRA

class WishFragment : Fragment(R.layout.fragment_wish) {

    lateinit var wishAdapter: PokemonListAdapter
    private lateinit var recyclerview: RecyclerView
    private lateinit var mainActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.connectViewComponent()
        initAdapter()
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        mainActivity = requireActivity() as? MainActivity ?: throw Exception("Unknown Activity")
        inflateWishData()
    }

    private fun View.connectViewComponent() {
        recyclerview = this@connectViewComponent.findViewById<RecyclerView>(R.id.rcv_wish_fragment)
    }

    private fun initAdapter() {
        wishAdapter = PokemonListAdapter()
            .apply {
                itemClickListener = object : PokemonItemClickListener {
                    override fun onItemClick(itemPosition: Int) {
                        val selectedItem = wishAdapter.currentList[itemPosition]
                        val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                            putExtra(TO_DETAIL_EXTRA, selectedItem)
                        }
                        startActivity(intent)
                    }

                    override fun onHeartClick(itemPosition: Int) {
                        val selectedItem = wishAdapter.currentList[itemPosition]
                        if (selectedItem.heart) removeWishItem(selectedItem)
                    }
                }
            }
    }

    private fun initRecyclerView() {
        //추후 화면 사이즈에 맞게 SpaneCount 설정
        recyclerview.layoutManager = GridLayoutManager(requireActivity(), 2)
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
        recyclerview.adapter = wishAdapter
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
}