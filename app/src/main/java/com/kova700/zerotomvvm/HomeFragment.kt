package com.kova700.zerotomvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

//TODO: 생성자에 Layout ID 넘겨서 xml 연결 가능
class HomeFragment : Fragment() {

    lateinit var homeAdapter: PokemonListAdapter
    private lateinit var recyclerview: RecyclerView
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)


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
                        Toast.makeText(activity, "Detail Activity로 데이터가지고 이동", Toast.LENGTH_SHORT)
                            .show()
                        //Detail Activity로 데이터가지고 이동

                    }

                    override fun onHeartClick(itemPosition: Int) {
                        addWishItem(homeAdapter.currentList[itemPosition].copy(heart = true))
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
                            else -> throw Exception() //올바른 예외로 수정
                        }
                    }
                }
            }
        recyclerview.adapter = homeAdapter
    }

    private fun addWishItem(pokemonListItem: PokemonListItem) {
        mainActivity.wishPokeymonList.add(pokemonListItem)
        pokemonListItem.heart = true
        mainActivity.wishNewDataFlag = true //WishFagment가 데이터 갱신이 필요함을 알아야함
    }

    private fun inflateDummyData() {
        homeAdapter.submitList(mainActivity.homePokeymonList.toList())
    }

}