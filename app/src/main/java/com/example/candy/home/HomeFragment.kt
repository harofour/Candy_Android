package com.example.candy.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.candy.adapter.HorizontalItemDecorator
import com.example.candy.adapter.VerticalItemDecorator
import com.example.candy.databinding.FragmentHomeBinding
import com.example.candy.home.adapter.CategoryAdapter
import com.example.candy.home.adapter.MyChallengeAdapter
import com.example.candy.model.data.Challenge

class HomeFragment : Fragment() {
    private var homeBinding: FragmentHomeBinding? = null   // onDestory 에서 완벽한 제거를 위해 null 허용
    private lateinit var homeViewModel: HomeViewModel

    companion object {
        const val TAG: String = "로그"

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeBinding = binding
        return homeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cAdapter = CategoryAdapter()
        val mAdapter = MyChallengeAdapter()

        // 카테고리
        homeBinding!!.rvCategory.apply {
            adapter = cAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizontalItemDecorator(15))
            setHasFixedSize(true)
        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getCategories().observe(viewLifecycleOwner) { data ->
            //update ui
            Log.d("HomeFragment", "getCategories / $data")
            cAdapter.setCategories(data)
        }

        // 진행중인 챌린지
        homeBinding!!.rvChallenge.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(VerticalItemDecorator(15))
            setHasFixedSize(true)
        }

        // 챌린지 임시
        val challenges: List<Challenge> = generateChallengeData()
        mAdapter.setChallenges(challenges)
    }

    private fun generateChallengeData(): List<Challenge> {
        return listOf<Challenge>(
            Challenge(1, "영어", false, 1, 1, "5형식", "1"),
            Challenge(1, "영어", false, 1, 1, "5형식", "1"),
            Challenge(1, "영어", false, 1, 1, "5형식", "1"),
            Challenge(1, "영어", false, 1, 1, "5형식", "1"),
            Challenge(1, "영어", false, 1, 1, "5형식", "1"),
            Challenge(1, "영어", false, 1, 1, "5형식", "1")
        )
    }

    override fun onDestroyView() {
        homeBinding = null
        super.onDestroyView()
    }

}