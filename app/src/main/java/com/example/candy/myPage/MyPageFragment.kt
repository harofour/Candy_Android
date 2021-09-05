package com.example.candy.myPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.candy.R
import com.example.candy.activity.LogInActivity
import com.example.candy.databinding.FragmentMypageBinding
import com.example.candy.model.data.Candy
import com.example.candy.model.viewModel.SharedViewModel
import com.example.candy.myPage.viewmodel.MyPageViewModel
import com.example.candy.utils.CurrentUser

class MyPageFragment: Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private val viewModel: MyPageViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by activityViewModels()

    private lateinit var navController: NavController

    companion object {
        const val TAG : String = "로그"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.user = viewModel.getUserInfo()
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        sharedViewModel.getCandyStudent().observe(viewLifecycleOwner, {
            val numberOfCandy = getString(R.string.numberOfStudentCandy, it)
            binding.candy = Candy(numberOfCandy)
        })

        // 마이페이지 하위 메뉴 클릭 시
        setMyPageMenu()

    } // onViewCreated()


    private fun setMyPageMenu() {
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

        // 로그아웃 버튼 클릭 시
        binding.logoutBtn.setOnClickListener {
            CurrentUser.userToken = null
            CurrentUser.userInfo = null
            val intent = Intent(activity, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}