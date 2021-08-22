package com.example.candy.myPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.candy.R
import com.example.candy.databinding.FragmentUserChangeBinding
import com.example.candy.model.data.UserInfo
import com.example.candy.myPage.MyPageFragment.Companion.TAG
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.RESPONSE_STATE
import com.example.candy.utils.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class UserChangeFragment : Fragment() {
    private lateinit var binding: FragmentUserChangeBinding
    private val viewModel: MyPageViewModel by viewModels()

    private lateinit var changeBirth: String
    private lateinit var changePhone: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_change,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this

        // Setting Title
        binding.titleBar.title.text = "정보 변경"
        binding.titleBar.backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Setting UserInfo
        viewModel.getAPIUserInfo(CurrentUser.userToken!!).observe(viewLifecycleOwner,{
            binding.user = it
        })

        // 변경하기 버튼이 클릭 되었을 때
        binding.changeUserInfoBtn.setOnClickListener {
            /*
             * 1. 생년월일과 전화번호를 변경할 계획임
             * 2. 버튼이 눌렸을 떄 생년월일과 전화번호에 대하여 유효성 검사를 한 후 맞지 않다면 error 를 띄워준다.
             * 3. 정상적인 값이 들어오면 api 요청을 보낸다.
             */
            if(validationCheck()){
                // 정상적인 값이 입력되었을 때 유저정보를 변경하는 코드
                changePhone = String.format("%s-%s-%s",changePhone.substring(0,3),changePhone.substring(3,7),changePhone.substring(7,11))
                changeBirth = String.format("%s-%s-%s",changeBirth.substring(0,4),changeBirth.substring(4,6),changeBirth.substring(6,8))
                val data = HashMap<String,Any>()
                data["birth"] = changeBirth
                data["name"] = binding.labelEditName.text.toString()
                data["phone"] = changePhone
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.updateUserInfo(CurrentUser.userToken!!,data) { responseState ->
                        when (responseState) {
                            RESPONSE_STATE.SUCCESS -> {
                                try{
                                    parentFragmentManager.popBackStack()
                                }catch (e : Exception){
                                    e.printStackTrace()
                                }
                            }
                            RESPONSE_STATE.FAILURE -> {
                                Util.toast(binding.root.context,"정보 변경에 실패하였습니다.")
                            }
                        }
                    }
                }
            }else{
                return@setOnClickListener
            }
        }

    }



    private fun validationCheck() : Boolean {
        /*
         * 생년월일과 전화번호에 대한 유효성 검사
         * 1. 생년월일 형식 20000101 -> 8자리 맞는지 검사
         * 2. 전화번호 형식 01012341234 -> 11자리 맞는지 검사
         */
        changeBirth = binding.labelEditBirth.text.toString() ?: ""
        changePhone = binding.labelEditPhone.text.toString() ?: ""
        if(changeBirth.length == 8 && changePhone.length == 11 && verifyPassword())
            return true
        if(changeBirth.length != 8){
            binding.textFieldBirth.error = "다시 입력해주세요. ex)20000101"
        }
        if(changePhone.length != 11){
            binding.textFieldPhone.error = "다시 입력해주세요. ex)01012341234"
        }
        if(!verifyPassword()){
            binding.textField4.error = "비밀번호를 확인해 주세요."
        }
        return false


    }

    // 비밀번호 확인 - API 완성 되기 전까지 임시
    // TODO:: API 완성되면 수정 MainActivity 함께 수정
    private fun verifyPassword(): Boolean {
        return binding.labelEditPw.text.toString() == CurrentUser.userPw
    }
}