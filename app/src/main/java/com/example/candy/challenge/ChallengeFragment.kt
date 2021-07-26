package com.example.candy.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.candy.databinding.FragmentChallengeBinding
import com.example.candy.databinding.FragmentHomeBinding
import com.example.candy.home.HomeFragment
import com.example.candy.viewModel.MainViewModel

class ChallengeFragment : Fragment() {

    private var challengeBinding : FragmentChallengeBinding? = null   // onDestory 에서 완벽한 제거를 위해 null 허용

    private val mainViewModel : MainViewModel by activityViewModels()    // 프래그먼트 간에 뷰 모델 공유하기 위해 액티비티의 뷰모델 인스턴스 사용

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
        return challengeBinding!!.root
    }




    override fun onDestroyView() {
        challengeBinding = null
        super.onDestroyView()
    }
}