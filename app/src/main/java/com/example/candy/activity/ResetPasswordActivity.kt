package com.example.candy.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.candy.Activitiy.BaseActivity
import com.example.candy.R
import com.example.candy.data.ApiBooleanResponse
import com.example.candy.databinding.ActivityFindPasswordBinding
import com.example.candy.databinding.ActivityResetPasswordBinding
import com.example.candy.retrofit.RetrofitManager
import com.example.candy.utils.REQUEST_TYPE
import com.example.candy.utils.RESPONSE_STATE
import com.example.candy.utils.Util
import com.google.gson.Gson

class ResetPasswordActivity : BaseActivity() {
    val Tag = "FindPasswordActivity"
    private var mBinding: ActivityResetPasswordBinding? = null
    private val binding get() = mBinding!!
    lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        with(supportActionBar!!) {
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "비밀번호 재설정"
        }

        id = intent.getStringExtra("email") ?: "error"
        binding.idTV.text = "아이디 : $id"

        initListeners()
    }

    fun initListeners() {
        with(binding) {
            finishButton.setOnClickListener {
                resetPassword()
            }
        }
    }

    private fun resetPassword() {
        // 새 비밀번호 문자열 확인
        with(binding){
            if(newPasswordET != newPasswordChectET) {
                Util.toast(applicationContext, "비밀번호가 다릅니다")
                Log.d(Tag, "비밀번호가 다름")
                return
            }
        }

        val data = HashMap<String,Any>()
        data.put("email",id)
        data.put("password",binding.newPasswordET.text.toString())

        RetrofitManager.instance.requestBoolean(data, REQUEST_TYPE.RESET_PASSWORD){ responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.SUCCESS -> {
                    val result = Gson().fromJson(responseBody, ApiBooleanResponse::class.java)
                    if(result.response){
                        // 비밀번호 재설정 성공
                        val intent = Intent(applicationContext,LogInActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        Util.toast(applicationContext, "비밀번호가 재설정 되었습니다")
                    }else{
                        Util.toast(applicationContext, "비밀번호 재설정에 실패하였습니다")
                    }
                }
                RESPONSE_STATE.FAILURE -> {
                    Util.toast(applicationContext, "RESPONSE_STATE.FAILURE / 존재하지 않는 아이디 입니다2")
                }
            }
        }
    }
}