package com.kova700.zerotomvvm

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kova700.zerotomvvm.DetailActivity.Companion.TO_MAIN_HEART_BOOLEAN_EXTRA
import com.kova700.zerotomvvm.DetailActivity.Companion.TO_MAIN_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_EXTRA

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var homeAdapter: PokemonListAdapter
    private lateinit var recyclerview: RecyclerView
    private lateinit var mainActivity: MainActivity
    private lateinit var itemAddBtn: FloatingActionButton
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent?>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as? MainActivity ?: throw Exception("Unknown Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.connectViewComponent()
        initAdapter()
        initRecyclerView()
        initActivityResultLauncher()
        inflateDummyData()
    }

    private fun View.connectViewComponent() {
        recyclerview = this@connectViewComponent.findViewById(R.id.rcv_home_fragment)
        itemAddBtn = this@connectViewComponent.findViewById(R.id.fab_home_fragment)
        itemAddBtn.setOnClickListener {
            val mainList = mainActivity.homePokeymonList
            mainList.add(getRandomDummyItem(mainList.size + 1))
            homeAdapter.submitList(mainList.toList())
        }
    }

    private fun initActivityResultLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode != RESULT_OK) return@registerForActivityResult

                val intent = result.data ?: throw Exception("DetailActivity result is not exist")
                val heartValue = intent.getBooleanExtraData(TO_MAIN_HEART_BOOLEAN_EXTRA)
                val itemPosition = intent.getIntExtraData(TO_MAIN_ITEM_POSITION_EXTRA)

                mainActivity.homePokeymonList[itemPosition] =
                    mainActivity.homePokeymonList[itemPosition].copy(heart = heartValue)
                homeAdapter.submitList(mainActivity.homePokeymonList.toList())
            }
    }

    private fun initAdapter() {
        homeAdapter = PokemonListAdapter()
            .apply {
                itemClickListener = object : PokemonItemClickListener {
                    override fun onItemClick(itemPosition: Int) {
                        val selectedItem = homeAdapter.currentList[itemPosition]
                        val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                            putExtra(TO_DETAIL_SELECTED_ITEM_EXTRA, selectedItem)
                            putExtra(TO_DETAIL_ITEM_POSITION_EXTRA, itemPosition)
                        }
                        activityResultLauncher.launch(intent)
                    }

                    override fun onHeartClick(itemPosition: Int) {
                        val selectedItem = homeAdapter.currentList[itemPosition]
                        mainActivity.homePokeymonList[itemPosition] =
                            selectedItem.copy(heart = !selectedItem.heart)
                        ///스크롤 아래로 내린 후 다시 위로 돌아올 때,
                        //ListAdapter내부 currentList에는 Heart값이 해당 아이템이 그대로 false인 채로  남아 있어서
                        //다시 뷰홀더에 데이터가 binding될 떄, 하트가 그대로 false로 남아 있는 현상이 발생합니다.
                        //PokemonListItem 클래스의 Heart 프로퍼티 값을 수정가능하게 var로 변경해서
                        //homeAdapter.currentList에 있는 아이템의 heart값을 수정해주면 이런 현상을 방지할 수 있습니다.

                        //하지만
                        //불변 값(val)으로 data class를 구성하는 게 데이터 전달시에 안정적이라는 것을 알기에
                        //불면 값으로 data class를 구성하고 매번 버튼을 누를 때마다, submitList를 해주는 게 맞는건지 고민입니다.

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