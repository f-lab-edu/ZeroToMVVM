package com.kova700.zerotomvvm

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.kova700.zerotomvvm.DetailActivity.Companion.TO_MAIN_HEART_BOOLEAN_EXTRA
import com.kova700.zerotomvvm.DetailActivity.Companion.TO_MAIN_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_ITEM_POSITION_EXTRA
import com.kova700.zerotomvvm.MainActivity.Companion.TO_DETAIL_SELECTED_ITEM_EXTRA
import com.kova700.zerotomvvm.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var homeAdapter: PokemonListAdapter
    private lateinit var mainActivity: MainActivity
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent?>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as? MainActivity ?: throw Exception("Unknown Activity")
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
        initAdapter()
        initRecyclerView()
        initActivityResultLauncher()
        inflateDummyData()
    }

    private fun setPlusBtnClickListener() {
        binding.fabHomeFragment.setOnClickListener {
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

    private fun inflateDummyData() {
        homeAdapter.submitList(mainActivity.homePokeymonList.toList())
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