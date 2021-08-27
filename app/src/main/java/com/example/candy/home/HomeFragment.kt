package com.example.candy.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.candy.R
import com.example.candy.adapter.HorizontalItemDecorator
import com.example.candy.adapter.VerticalItemDecorator
import com.example.candy.databinding.FragmentHomeBinding
import com.example.candy.home.adapter.CategoryAdapter
import com.example.candy.home.adapter.MyChallengeAdapter

class HomeFragment : Fragment() {
    private var homeBinding: FragmentHomeBinding? = null   // onDestory 에서 완벽한 제거를 위해 null 허용
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var navController: NavController

    companion object {
        const val TAG: String = "로그"

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeBinding = binding
        return homeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val cAdapter = CategoryAdapter { pos -> onCategoryItemClicked(pos) }
        val mAdapter = MyChallengeAdapter { pos -> onChallengeItemClicked(pos) }

        // 카테고리
        homeBinding!!.rvCategory.apply {
            adapter = cAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizontalItemDecorator(15))
            setHasFixedSize(true)
        }
        // 진행중인 챌린지
        homeBinding!!.rvChallenge.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(VerticalItemDecorator(15))
            setHasFixedSize(true)
        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // 카테고리
        homeViewModel.getCategories().observe(viewLifecycleOwner) { data ->
            //update ui
            Log.d("HomeFragment", "getCategories / $data")
            cAdapter.setCategories(data)
        }
        // 진행중인 챌린지
        homeViewModel.getOnGoingChallenges().observe(viewLifecycleOwner) { data ->
            //update ui
            Log.d("HomeFragment", "getCategories / $data")
            mAdapter.setChallenges(data)
        }
    }

    override fun onDestroyView() {
        homeBinding = null
        super.onDestroyView()
    }

    private fun onCategoryItemClicked(position: Int) {
        Log.d("HomeFragment", "CategoryItemClicked() position $position")
        homeViewModel.sortChallengeByCategory(position)
        // 진행중인 챌린지 리스트 정렬
    }

    private fun onChallengeItemClicked(position: Int) {
        Log.d("HomeFragment", "ChallengeItemClicked() position $position")
        // 강의 화면으로 이동
        navController.navigate(R.id.action_homeFragment_to_challengeLectureFragment)
    }
}