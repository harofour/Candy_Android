package com.example.candy.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.candy.R
import com.example.candy.data.ApiBooleanResponse
import com.example.candy.databinding.ActivityFindPasswordBinding
import com.example.candy.retrofit.RetrofitManager
import com.example.candy.utils.REQUEST_TYPE
import com.example.candy.utils.RESPONSE_STATE
import com.example.candy.utils.Util
import com.google.gson.Gson

class FindPasswordActivity : BaseActivity() {
    val Tag = "FindPasswordActivity"
    private var mBinding: ActivityFindPasswordBinding? = null
    private val binding get() = mBinding!!

    private var isEmailAuthSended = false
    private var isEmailAuthChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFindPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        with(supportActionBar!!){
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "비밀번호 찾기"
        }

        initListeners()
    }

    private fun initListeners(){
        with(binding){
            sendEmailAuthBtn.setOnClickListener {
                sendAuth()
            }

            finishButton.setOnClickListener {
                checkAuth()
            }
        }
    }

    private fun checkAuth() {
        if(!isEmailAuthSended){
            Util.toast(applicationContext, "인증 메일을 전송해 주세요")
            Log.d(Tag,"인증 메일 전송 x / isEmailAuthSenede : $isEmailAuthSended")
            return
        }
        if(isEmailAuthChecked){
            Util.toast(applicationContext, "이메일 인증이 완료되었습니다")
            Log.d(Tag,"이미 인증코드 확인됨 / isEmailAuthChecked : $isEmailAuthChecked")
            return
        }

        val data = HashMap<String, Any>()
        with(binding){
            data.put("email",emailET.text.toString())
            data.put("auth",emailAuthET.text.toString())
        }
        RetrofitManager.instance.requestBoolean(data, REQUEST_TYPE.CHECK_AUTH){ responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.SUCCESS -> { // 인증코드 확인 성공
                    val result = Gson().fromJson(responseBody, ApiBooleanResponse::class.java)
                    if(result.response){ // 확인 결과 인증 성공
                        val newIntent = Intent(applicationContext, ResetPasswordActivity::class.java)
                        val id = binding.emailET.text.toString()
                        newIntent.putExtra("email",id)
                        startActivity(newIntent)
                    }else{  // 인증 실패
                        Util.toast(applicationContext, "인증 코드가 다릅니다")
                    }
                }
                RESPONSE_STATE.FAILURE -> {
                    Util.toast(applicationContext, "인증 코드 또는 이메일이 다릅니다")
                }
            }
        }
    }

    private fun sendAuth() {
        if(isEmailAuthSended){
            Util.toast(applicationContext, "이미 전송되었습니다")
            Log.d(Tag,"인증 메일이 이미 전송됨 / isEmailAuthSended : $isEmailAuthSended")
            return
        }

        val data = HashMap<String,Any>()
        data.put("email",binding.emailET.text.toString())

        RetrofitManager.instance.requestBoolean(data, REQUEST_TYPE.SEND_AUTH){ responseSTate, responseBody ->
            when(responseSTate){
                RESPONSE_STATE.SUCCESS -> { // 메일 전송 성공
                    val result = Gson().fromJson(responseBody, ApiBooleanResponse::class.java)
                    isEmailAuthSended = result.response
                    if(result.response){
                        Util.toast(applicationContext, "메일이 전송 되었습니다")
                    }else{
                        Util.toast(applicationContext, "존재하지 않는 아이디 입니다")
                    }
                }
                RESPONSE_STATE.FAILURE -> {
                    Util.toast(applicationContext, "존재하지 않는 아이디 입니다2")
                    isEmailAuthSended = false
                }
            }
        }
    }

    private fun timeToString(time: Int) : String {
        var min = ""
        var sec = ""
        if(time>120){
            min = "2"
            sec = "${time-120}"
        }else if(time>60){
            min = "1"
            sec = "${time-60}"
        }else{
            min = "0"
            sec = "$time"
        }
        return "$min : $sec"
    }
}