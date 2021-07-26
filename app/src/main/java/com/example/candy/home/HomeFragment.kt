package com.example.candy.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.candy.databinding.FragmentHomeBinding
import com.example.candy.databinding.FragmentMypageBinding

class HomeFragment : Fragment() {

    private var homeBinding : FragmentHomeBinding? = null   // onDestory 에서 완벽한 제거를 위해 null 허용

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



    override fun onDestroyView() {
        homeBinding = null
        super.onDestroyView()
    }

}