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

    private lateinit var homeAdapter: PokemonListAdapter
    private lateinit var recyclerview: RecyclerView
    private val dummy = getDummy()

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
//        initGettingEvent()
    }

    private fun View.connectViewComponent() {
        recyclerview = this@connectViewComponent.findViewById<RecyclerView>(R.id.rcv_home_fragment)
    }

    private fun initAdapter() {
        homeAdapter = PokemonListAdapter()
        homeAdapter.itemClickListener = object : PokemonItemClickListener {
            override fun onItemClick(itemPosition: Int) {
                val selectedItem = homeAdapter.currentList[itemPosition]
                Toast.makeText(activity, "Detail Activity로 데이터가지고 이동", Toast.LENGTH_SHORT).show()
                //Detail Activity로 데이터가지고 이동

            }

            override fun onHeartClick(itemPosition: Int) {
                //Adapter가 가지고 있는 데이터 상태 변경하고 그 아이템 바인딩 다시해서 하트 표시 다시하기
                Toast.makeText(activity, "하트 눌림", Toast.LENGTH_SHORT).show()
//                homeAdapter.currentList[itemPosition]
            }

        }
    }

    private fun initRecyclerView() {
        //추후 화면 사이즈에 맞게 SpaneCount 설정
        recyclerview.layoutManager = GridLayoutManager(activity, 2)
            .apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        //추후에 다른 뷰타입이 추가된다면 할당되는 칸 수 조절
                        return when (homeAdapter.getItemViewType(position)) {
                            R.layout.item_home -> 1
                            else -> throw Exception() //올바른 예외로 수정
                        }
                    }
                }
            }
        recyclerview.adapter = homeAdapter
    }

    private fun inflateDummyData() {
        homeAdapter.submitList(dummy)
    }

//    //변화값 주고 받을 떄 이런 형식 가능할듯
//    private fun initGettingEvent() {
//        val result = Bundle().apply {
////            putString("newWishItem", "결과")
//        }
//        parentFragmentManager.setFragmentResult("newWishItemKey", result)
//    }

}