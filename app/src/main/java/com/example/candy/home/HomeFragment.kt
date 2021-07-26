package com.example.candy.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.candy.adapter.HomeCategoryRecyclerAdapter
import com.example.candy.adapter.HomeChallengeOngoingRecyclerAdapter
import com.example.candy.data.DataHomeChallengeOngoing
import com.example.candy.databinding.FragmentHomeBinding
import com.example.candy.databinding.FragmentMypageBinding
import com.example.candy.viewModel.MainViewModel

class HomeFragment : Fragment() {

    private var homeBinding : FragmentHomeBinding? = null   // onDestory 에서 완벽한 제거를 위해 null 허용
    private val categoryList = arrayListOf<String>()
    private val challengeOngoingList = arrayListOf<DataHomeChallengeOngoing>()

    private val mainViewModel : MainViewModel by activityViewModels()    // 프래그먼트 간에 뷰 모델 공유하기 위해 액티비티의 뷰모델 인스턴스 사용

    companion object {
        const val TAG : String = "로그"

        fun newInstance() : HomeFragment {
            return HomeFragment()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeBinding = binding
        return homeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 카테고리 recycler
        categoryList.add("ALL")
        categoryList.add("영어")
        categoryList.add("수학")
        categoryList.add("국어")
        categoryList.add("과학")
        categoryList.add("한국사")


        homeBinding!!.homeRecyclerviewCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeBinding!!.homeRecyclerviewCategory.adapter = HomeCategoryRecyclerAdapter(categoryList)


        // 진행 중 챌린지 recycler   //테스트용 데이터
        challengeOngoingList.add(DataHomeChallengeOngoing("영어", "5형식", "5형식 동사에 대한 이해",
                8, 90))

        challengeOngoingList.add(DataHomeChallengeOngoing("영어", "5형식", "5형식 동사에 대한 이해",
                8, 90))

        challengeOngoingList.add(DataHomeChallengeOngoing("영어", "5형식", "5형식 동사에 대한 이해",
                8, 90))

        challengeOngoingList.add(DataHomeChallengeOngoing("영어", "5형식", "5형식 동사에 대한 이해",
                8, 90))

        challengeOngoingList.add(DataHomeChallengeOngoing("영어", "5형식", "5형식 동사에 대한 이해",
                8, 90))

        challengeOngoingList.add(DataHomeChallengeOngoing("영어", "5형식", "5형식 동사에 대한 이해",
                8, 90))

        challengeOngoingList.add(DataHomeChallengeOngoing("영어", "5형식", "5형식 동사에 대한 이해",
                8, 90))

        homeBinding!!.homeRecyclerviewChallengeOngoing.layoutManager = LinearLayoutManager(context)
        homeBinding!!.homeRecyclerviewChallengeOngoing.adapter = HomeChallengeOngoingRecyclerAdapter(challengeOngoingList)


        // 진행 중 챌린지 리스트 livedata observe
        //
        //
        //
    }



    override fun onDestroyView() {
        homeBinding = null
        super.onDestroyView()
    }

}