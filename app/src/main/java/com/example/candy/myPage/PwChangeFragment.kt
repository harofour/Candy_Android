package com.example.candy.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.candy.R
import com.example.candy.databinding.FragmentPwChangeBinding
import com.example.candy.myPage.viewmodel.MyPageViewModel
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.RESPONSE_STATE
import com.example.candy.utils.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PwChangeFragment : Fragment() {
    private lateinit var binding: FragmentPwChangeBinding
    private val viewModel: MyPageViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pw_change, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.titleBar.title.text = "비밀번호 변경"
        binding.titleBar.backBtn.setOnClickListener {
            navController.popBackStack()
        }

        // 비밀번효 변경 버튼 클릭 시
        binding.changePwBtn.setOnClickListener {
            val currentPw = binding.labelEditCurrentPw.text.toString()
            val newPw: String = binding.labelEditNewPw.text.toString()
            val verifyPw = binding.labelEditVerifyPw.text.toString()
            val data = HashMap<String, Any>()
            data["newPassword"] = newPw
            data["originPassword"] = currentPw
            if (newPw == verifyPw) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.changePw(CurrentUser.userToken!!, data) { responseState ->
                        when (responseState) {
                            RESPONSE_STATE.SUCCESS -> {
                                try {
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            RESPONSE_STATE.FAILURE -> {
                                Util.showErrorAlertDialog(
                                    binding.root.context,
                                    "비밀번호 변경 실패",
                                    "현재 비밀번호를 확인해주세요."
                                )
                            }
                        }
                    }
                }
            } else {
                binding.textFieldVerifyPw.error = "비밀번호가 일치하지 않습니다!"
            }
        }

    }
}