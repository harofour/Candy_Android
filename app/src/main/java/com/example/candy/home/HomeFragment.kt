package com.example.candy.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.R
import com.example.candy.adapter.HorizontalItemDecorator
import com.example.candy.adapter.VerticalItemDecorator
import com.example.candy.challenge.ChallengeLectureActivity
import com.example.candy.databinding.FragmentHomeBinding
import com.example.candy.home.adapter.CategoryAdapter
import com.example.candy.home.adapter.MyChallengeAdapter
import com.example.candy.model.viewModel.SharedViewModel

class HomeFragment : Fragment() {
    private lateinit var challengeAdapter: MyChallengeAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private var homeBinding: FragmentHomeBinding? = null   // onDestory 에서 완벽한 제거를 위해 null 허용
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return with(modelClass) {
                    when {
                        isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel.getInstance()
                        else -> throw IllegalArgumentException("Unknown viewModel class $modelClass")
                    }
                } as T
            }
        }).get(HomeViewModel::class.java)
    }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var navController: NavController
    private var page = 1 // 현재 페이지
    private var size = 10 // 한번에 불러 올 챌린지 리스트 수
    private var NO_MORE_DATA = false // 서버로부터 데이터를 받아온 뒤 true

    companion object {
        private lateinit var instance: HomeFragment

        @MainThread
        fun getInstance(): HomeFragment {
            instance = if (::instance.isInitialized) instance else HomeFragment()
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HomeFragment", "onCreateView")
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeBinding = binding

        sharedViewModel.getCandyStudent().observe(viewLifecycleOwner, {
            homeBinding!!.tvMyCandy.text = getString(R.string.myCandy, it)
        })

        return homeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "onViewCreated")

        navController = Navigation.findNavController(view)

        categoryAdapter = CategoryAdapter { pos -> onCategoryItemClicked(pos) }
        challengeAdapter = MyChallengeAdapter { pos -> onChallengeItemClicked(pos) }

        initCategory()
        initOnGoingChallenge()
    }

    override fun onResume() {
        super.onResume()
        Log.d("HomeFragment", "onResume()")
    }


    private fun initCategory() {
        homeBinding!!.rvCategory.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizontalItemDecorator(15))
            setHasFixedSize(true)
        }

        homeViewModel.getCategories().observe(viewLifecycleOwner) { data ->
            //update ui
            Log.d("HomeFragment", "getCategories / $data")
            categoryAdapter.setCategories(data)
        }
    }

    private fun initOnGoingChallenge() {
        homeBinding!!.rvChallenge.apply {
            adapter = challengeAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(VerticalItemDecorator(15))
            setHasFixedSize(true)

            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    // 스크롤이 끝에 도달했는지 확인
                    // 화면에 보이는 마지막 아이템의 position
                    val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()

                    // 어댑터에 등록된 아이템의 총 개수 -1
                    val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                    // 스크롤이 끝에 도달했는지 확인
                    if (lastVisibleItemPosition == itemTotalCount && !homeBinding!!.rvChallenge.canScrollVertically(1)) {
                        Log.d("rvChallenge onScrolled()", "끝, itemTotalCount / $itemTotalCount ")

                        Log.d("rvChallenge onScrolled()", "LastChallengeId : ${challengeAdapter.getLastChallengeId()}")
                        homeViewModel.getOnGoingChallenges(challengeAdapter.getLastChallengeId(), size, categoryAdapter.getCurrentCategory())
                    }
                }
            })
        }

        homeViewModel.getOnGoingChallenges(challengeAdapter.getLastChallengeId(), size, categoryAdapter.getCurrentCategory()).observe(viewLifecycleOwner) { data ->
            //update ui
            Log.d("HomeFragment", "1getChallenges ${data.size} / $data")
            challengeAdapter.addLoading()
            challengeAdapter.setList(data)

            // 한 페이지당 게시물이 10개씩 들어있음.
            // 새로운 게시물이 추가되었다는 것을 알려줌 (추가된 부분만 새로고침)
            challengeAdapter.notifyItemRangeChanged((page-1)*size ,data.size)
            challengeAdapter.deleteLoading()
        }
    }

    private fun onCategoryItemClicked(position: Int) {
        Log.d("HomeFragment", "CategoryItemClicked() position $position")
        homeViewModel.sortChallengeByCategory(categoryAdapter.getCategory(position))
        categoryAdapter.changeCategory(position)
        page = 0
        // 진행중인 챌린지 리스트 정렬
    }

    private fun onChallengeItemClicked(position: Int) {
        Log.d("HomeFragment", "ChallengeItemClicked() position $position")
        // 강의 화면으로 이동
        val challenge = homeViewModel.getChallenge(position)
        val intent =
            Intent(requireActivity().applicationContext, ChallengeLectureActivity::class.java)
        intent.putExtra("Challenge", challenge)
        requireActivity().startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("HomeFragment", "onDestroyView")
        homeViewModel.clearOnGoingChallenges()
    }
}

