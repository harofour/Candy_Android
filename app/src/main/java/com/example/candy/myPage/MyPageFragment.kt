package com.example.candy.myPage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.candy.R
import com.example.candy.databinding.FragmentMypageBinding
import com.example.candy.utils.CurrentUser

class MyPageFragment: Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private val viewModel: CandyViewModel by viewModels()

    // 하위 메뉴 프래그먼트
    private lateinit var studentCandyFragment: StudentCandyFragment
    private lateinit var parentCandyFragment: ParentCandyFragment

    companion object {
        const val TAG : String = "로그"

        fun newInstance() : MyPageFragment {
            return MyPageFragment()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.user = viewModel.getUserInfo()


        viewModel.getAPICandyStudent(CurrentUser.userToken!!)
        viewModel.getAPICandyParent(CurrentUser.userToken!!)

        viewModel.getCandyStudent().observe(viewLifecycleOwner,{
            binding.candy = it
        })

        // 마이페이지 하위 메뉴 클릭 시
        setMypageMenu()

    } // onViewCreated()


    private fun setMypageMenu() {
        // 캔디 인출 메뉴 클릭 시
        binding.withdrawCandy.setOnClickListener {
            studentCandyFragment = StudentCandyFragment()
            parentFragmentManager.beginTransaction()
                .add(R.id.framelayout_main,studentCandyFragment)
                .addToBackStack(null)
                .commit()
        }

        // 캔디 충전 메뉴 클릭 시
        binding.chargeCandy.setOnClickListener {
            parentCandyFragment = ParentCandyFragment()
            parentFragmentManager.beginTransaction()
                .add(R.id.framelayout_main,parentCandyFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}