package com.example.candy.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.candy.Activitiy.BaseActivity
import com.example.candy.GMailSender
import com.example.candy.R
import com.example.candy.data.ApiBooleanResponse
import com.example.candy.data.ApiStringResponse
import com.example.candy.databinding.ActivityFindPasswordBinding
import com.example.candy.retrofit.RetrofitManager
import com.example.candy.utils.REQUEST_TYPE
import com.example.candy.utils.RESPONSE_STATE
import com.example.candy.utils.Util
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FindPasswordActivity : BaseActivity() {
    val Tag = "FindPasswordActivity"
    private var mBinding: ActivityFindPasswordBinding? = null
    private val binding get() = mBinding!!

    private var gmailCode: String? = null
    private var isEmailSended = false

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
                val data = HashMap<String,Any>()
                with(binding){
                    data.put("email",emailET.text.toString())
                }

                RetrofitManager.instance.requestBoolean(data, REQUEST_TYPE.SEND_AUTH){ responseSTate, responseBody ->
                    when(responseSTate){
                        RESPONSE_STATE.SUCCESS -> { // 메일 전송 성공
                            val result = Gson().fromJson(responseBody, ApiBooleanResponse::class.java)
                            if(result.response){
//                                //인증 메일 전송
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    Util.sendMail(applicationContext, emailET.text.toString())?.let{
//                                        gmailCode = it
//                                    }
//                                }
                                isEmailSended = true
                                Util.toast(applicationContext, "메일이 전송 되었습니다")
                            }else{
                                Util.toast(applicationContext, "존재하지 않는 아이디 입니다")
                            }
                        }
                        RESPONSE_STATE.FAILURE -> {
                            Util.toast(applicationContext, "존재하지 않는 아이디 입니다2")
                        }
                    }

                }
//                sendEmailAuthBtn.isClickable = false

//                // 인증 메일 시간 제한 타이머
//                val times = 5
//                Util.timer(
//                    times = times,
//                    timeMillis = 1000,
//                    action = {
//                        Log.d(Tag, "action / ${timeToString(times - it)} / ${sendEmailAuthBtn.isClickable}")
//                        runOnUiThread{
//                            sendEmailAuthBtn.alpha = 0.5f
//                        }
//                    },
//                    finished= {
//                        sendEmailAuthBtn.isClickable = true
//
//                        runOnUiThread{
//                            sendEmailAuthBtn.alpha = 1f
//                        }
//                    }
//                )
            }

            finishButton.setOnClickListener {
                if(isEmailSended){
                    // for test
//                    val intent = Intent(applicationContext, ResetPasswordActivity::class.java)
//                    intent.putExtra("email","ddhtyuu@gmail.com")
//                    startActivity(intent)

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

                    // 이전
//                if(gmailCode != null){
//                    if(gmailCode == binding.emailAuthET.text.toString()){
//                        val intent = Intent(applicationContext, ResetPasswordActivity::class.java)
//                        val id = binding.emailET.text.toString()
//                        intent.putExtra("email",id)
//                        startActivity(intent)
//                    }else{
//                        Util.toast(applicationContext, "인증 코드가 다릅니다")
//                    }
//                }else{
//                    Util.toast(applicationContext, "인증 메일을 전송해 주세요")
//                }
                }else{
                    Util.toast(applicationContext, "인증 메일을 전송해 주세요")
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