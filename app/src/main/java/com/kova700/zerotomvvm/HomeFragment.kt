package com.kova700.zerotomvvm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_EXTRA

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var homeAdapter: PokemonListAdapter
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
        inflateDummyData()
    }

    private fun View.connectViewComponent() {
        recyclerview = this@connectViewComponent.findViewById<RecyclerView>(R.id.rcv_home_fragment)
    }

    private fun initAdapter() {
        homeAdapter = PokemonListAdapter()
            .apply {
                itemClickListener = object : PokemonItemClickListener {
                    override fun onItemClick(itemPosition: Int) {
                        val selectedItem = homeAdapter.currentList[itemPosition]
                        val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                            putExtra(TO_DETAIL_EXTRA, selectedItem)
                        }
                        startActivity(intent)
                    }

                    override fun onHeartClick(itemPosition: Int) {
                        val selectedItem = homeAdapter.currentList[itemPosition]
                        mainActivity.homePokeymonList[itemPosition] =
                            selectedItem.copy(heart = !selectedItem.heart)
                        //스크롤 이동 후 다시 돌아올 때, Heart값이 ListAdapter에는 그대로 false로 남아 있어서
                        //다시 binding될 떄, 하트가 원래 값으로 돌아오는 현상이 있어서
                        //PokemonListItem 클래스의 Heart 프로퍼티 값을 수정가능하게 var로 변경
                        homeAdapter.currentList[itemPosition].heart = !selectedItem.heart
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
                        return when (homeAdapter.getItemViewType(position)) {
                            R.layout.item_pokemon_list -> 1
                            else -> throw Exception("Unknown Item Layout")
                        }
                    }
                }
            }
        recyclerview.adapter = homeAdapter
    }

    private fun inflateDummyData() {
        homeAdapter.submitList(mainActivity.homePokeymonList.toList())
    }
}