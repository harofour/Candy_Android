package com.example.candy.challenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.candy.challenge.adapter.PagerFragmentStateAdapter
import com.example.candy.challenge.pagerFragment.CompleteFragment
import com.example.candy.challenge.pagerFragment.LikeFragment
import com.example.candy.challenge.pagerFragment.PossibleFragment
import com.example.candy.databinding.FragmentChallengeBinding
import com.example.candy.databinding.FragmentHomeBinding
import com.example.candy.home.HomeFragment
import com.example.candy.viewModel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ChallengeFragment : Fragment() {

    private var challengeBinding : FragmentChallengeBinding? = null   // onDestory 에서 완벽한 제거를 위해 null 허용

    //private val mainViewModel : MainViewModel by activityViewModels()    // 프래그먼트 간에 뷰 모델 공유하기 위해 액티비티의 뷰모델 인스턴스 사용

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    companion object {
        const val TAG : String = "로그"

        fun newInstance() : ChallengeFragment {
            return ChallengeFragment()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val binding = FragmentChallengeBinding.inflate(inflater, container, false)
        challengeBinding = binding

        viewPager = challengeBinding!!.viewPager
        tabLayout = challengeBinding!!.tabLayout

        return challengeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = PagerFragmentStateAdapter(requireActivity())

        pagerAdapter.addFragment(PossibleFragment())
        pagerAdapter.addFragment(LikeFragment())
        pagerAdapter.addFragment(CompleteFragment())

        // viewpager2 adapter 연결
        viewPager?.adapter = pagerAdapter
        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int){
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position+1}")
            }
        })

        // tablayout 연결
        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            tab.text = when(position){
                0 -> "도전 가능 챌린지"
                1 -> "찜한 챌린지"
                2 -> "완료한 챌린지"
                else -> {"TAB"}
            }
        }.attach()

        Log.d("fragment check","ChallengeFragment onViewCreated")
    }




    override fun onDestroyView() {
        challengeBinding = null
        super.onDestroyView()
    }
}