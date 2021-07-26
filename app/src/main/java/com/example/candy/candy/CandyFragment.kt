package com.example.candy.candy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.candy.databinding.FragmentCandyBinding
import com.example.candy.databinding.FragmentChallengeBinding
import com.example.candy.home.HomeFragment

class CandyFragment: Fragment() {

    private var candyBinding : FragmentCandyBinding? = null   // onDestory 에서 완벽한 제거를 위해 null 허용

    //private val mypageBinding get() = _mypageBinding!! // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재선언

    companion object {
        const val TAG : String = "로그"

        fun newInstance() : CandyFragment {
            return CandyFragment()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val binding = FragmentCandyBinding.inflate(inflater, container, false)
        candyBinding = binding
        return candyBinding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        candyBinding = null
        super.onDestroyView()
    }
}