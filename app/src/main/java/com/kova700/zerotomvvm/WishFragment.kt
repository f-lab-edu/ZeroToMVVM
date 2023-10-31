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
class WishFragment : Fragment() {

    lateinit var wishAdapter: PokemonListAdapter
    private lateinit var recyclerview: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_wish, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.connectViewComponent()
        initAdapter()
        initRecyclerView()
//        initFragmentResultListener()
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
        val mainActivity = (requireActivity() as? MainActivity) ?: return
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
        val mainActivity = (requireActivity() as? MainActivity) ?: return
        wishAdapter.submitList(mainActivity.wishPokeymonList.toList())
    }
//
//    //wish에 삭제될 애들이 있을 수 있으니 새로고침 Flag 실어서 보내줌 (Wish에서 사라진 애들이 있을 경우 == 목록에서 빈하트 처리해줘야할 애들)
//    private fun addFragmentResult() {
//        parentFragmentManager.setFragmentResult("deleteWish", bundleOf("deleteflag" to true))
//    }
//
//    //새로 추가된 애들이 있을수 있으니 새로고침 Flag확인하고 화면 갱신 (Wish에 추가될 애들 == 리스트에 넣어줄 애들)
//    private fun initFragmentResultListener() {
//        parentFragmentManager.setFragmentResultListener("addWish", this) { key, bundle ->
//            Toast.makeText(activity, "Wish 데이터 갱신", Toast.LENGTH_SHORT).show()
//            bundle.getString("addflag") ?: return@setFragmentResultListener
//            inflateWishData()
//        }
//    }
}