package com.example.candy.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.candy.adapter.HorizontalItemDecorator
import com.example.candy.adapter.VerticalItemDecorator
import com.example.candy.databinding.FragmentHomeBinding
import com.example.candy.home.adapter.CategoryAdapter
import com.example.candy.home.adapter.MyChallengeAdapter
import com.example.candy.model.data.Challenge

class HomeFragment : Fragment() {

    private var homeBinding : FragmentHomeBinding? = null   // onDestory 에서 완벽한 제거를 위해 null 허용

    companion object {
        const val TAG : String = "로그"

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeBinding = binding
        return homeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cAdapter = CategoryAdapter()
        val mAdapter = MyChallengeAdapter()

        homeBinding!!.rvCategory.apply {
            adapter = cAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(HorizontalItemDecorator(15))
            setHasFixedSize(true)
        }

        homeBinding!!.rvChallenge.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            addItemDecoration(VerticalItemDecorator(15))
            setHasFixedSize(true)
        }

        // 챌린지 임시
        val challenges : List<Challenge> = generateChallengeData()
        mAdapter.setChallenges(challenges)

        // 카테고리 임시
        val categories : List<String> = generateCategoryData()
        cAdapter.setCategories(categories)
    }

    private fun generateChallengeData(): List<Challenge> {
        return listOf<Challenge>(
            Challenge(1,"영어",false,1,1,"5형식","1"),
            Challenge(1,"영어",false,1,1,"5형식","1"),
            Challenge(1,"영어",false,1,1,"5형식","1"),
            Challenge(1,"영어",false,1,1,"5형식","1"),
            Challenge(1,"영어",false,1,1,"5형식","1"),
            Challenge(1,"영어",false,1,1,"5형식","1")
        )
    }

    private fun generateCategoryData(): List<String> {
        return listOf<String>(
            "영어","수학","물리","화학","한국사"
        )
    }


    override fun onDestroyView() {
        homeBinding = null
        super.onDestroyView()
    }

}