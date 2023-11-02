package com.kova700.zerotomvvm

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
                        Toast.makeText(activity, "Detail Activity로 데이터가지고 이동", Toast.LENGTH_SHORT)
                            .show()

                    }

                    override fun onHeartClick(itemPosition: Int) {
                        removeWishItem(itemPosition)
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
                        //추후에 다른 뷰타입이 추가된다면 할당되는 칸 수 조절
                        return when (wishAdapter.getItemViewType(position)) {
                            R.layout.item_pokemon_list -> 1
                            else -> throw Exception() //올바른 예외로 수정
                        }
                    }
                }
            }
        recyclerview.adapter = wishAdapter
    }

    private fun removeWishItem(position: Int) {
        val deletedPokemonItem = mainActivity.wishPokeymonList.removeAt(position)
        mainActivity.homePokeymonList.forEachIndexed { index, pokemonListItem ->
            if (pokemonListItem.pokemon.name == deletedPokemonItem.pokemon.name) {
                mainActivity.homePokeymonList[index] = deletedPokemonItem.copy(heart = false)
            }
        }
        mainActivity.homeNewDataFlag = true //HomeFagment가 데이터 갱신이 필요함을 알아야함
        wishAdapter.submitList(mainActivity.wishPokeymonList.toList())
    }

    private fun inflateWishData() {
        wishAdapter.submitList(mainActivity.wishPokeymonList.toList())
    }
}