package com.kova700.zerotomvvm

import android.os.Bundle
import android.util.Log
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
        inflateDummyData()
//        initFragmentResultListener()
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
        val mainActivity = (requireActivity() as? MainActivity) ?: return
        mainActivity.wishPokeymonList.add(pokemonListItem)
        pokemonListItem.heart = true
        mainActivity.wishNewDataFlag = true //WishFagment가 데이터 갱신이 필요함을 알아야함
    }

    private fun inflateDummyData() {
        val mainActivity = (requireActivity() as? MainActivity) ?: return
        homeAdapter.submitList(mainActivity.homePokeymonList.toList())
    }

//    //wish에 추가된 애들이 있을 수 있으니 새로고침 Flag 보내줌 (Wish에 추가될 애들이 있는경우)
//    //parentFragmentManager.setFragmentResult("addWish", result)로 데이터 전달
//    private fun addFragmentResult() {
//        parentFragmentManager.setFragmentResult("addWish", bundleOf("addflag" to true))
//    }
//
//    //wish에서 삭제된 애들이 있을 수 있으니 Flag확인 (Wish에서 제거될 애들 == 하트 비워줘야하는 애들)
//    private fun initFragmentResultListener() {
//        parentFragmentManager.setFragmentResultListener("deleteWish", this) { key, bundle ->
//            Toast.makeText(activity, "Home 데이터 갱신", Toast.LENGTH_SHORT).show()
//            bundle.getString("deleteflag") ?: return@setFragmentResultListener
//            inflateDummyData()
//        }
//    }

}