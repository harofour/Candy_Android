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
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.candy.R
import com.example.candy.databinding.FragmentMypageBinding
import com.example.candy.utils.CurrentUser

class MyPageFragment: Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private val viewModel: MyPageViewModel by viewModels()

    private lateinit var navController: NavController

    companion object {
        const val TAG : String = "로그"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage,container,false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.user = viewModel.getUserInfo()
        viewModel.getAPICandyStudent(CurrentUser.userToken!!)
        viewModel.getAPICandyParent(CurrentUser.userToken!!)
        viewModel.getCandyStudent().observe(viewLifecycleOwner,{
            binding.candy = it
        })

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        // 마이페이지 하위 메뉴 클릭 시
        setMypageMenu()

    } // onViewCreated()


    private fun setMypageMenu() {
        // 캔디 인출 메뉴 클릭 시
        binding.withdrawCandy.setOnClickListener {
            navController
                .navigate(R.id.action_myPageFragment_to_studentCandyFragment)
        }

        // 캔디 충전 메뉴 클릭 시
        binding.chargeCandy.setOnClickListener {
            navController
                .navigate(R.id.action_myPageFragment_to_parentCandyFragment)
        }

        // 정보 변경 메뉴 클릭 시
        binding.userChangeBtn.setOnClickListener {
            navController
                .navigate(R.id.action_myPageFragment_to_userChangeFragment)
        }

        // 비밀번호 변경 메뉴 클릭 시
        binding.pwChangeBtn.setOnClickListener {
            navController
                .navigate(R.id.action_myPageFragment_to_pwChangeFragment)
        }

        // 챌린지 강의 확인하는 임시버튼
        // TODO:: 테스트 확인 후 삭제
        binding.tempBtn.setOnClickListener {
            navController
                .navigate(R.id.action_myPageFragment_to_challengeLectureFragment)
        }
    }
}