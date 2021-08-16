package com.example.candy.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import com.example.candy.Activitiy.BaseActivity
import com.example.candy.data.ApiUserResponse
import com.example.candy.data.User
import com.example.candy.databinding.ActivityLogInBinding
import com.example.candy.retrofit.RetrofitManager
import com.example.candy.utils.REQUEST_TYPE
import com.example.candy.utils.RESPONSE_STATE
import com.example.candy.utils.Util
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.*


class LogInActivity : BaseActivity() {
    private val Tag: String = "LogInActivity"

    private var mBinding: ActivityLogInBinding? = null
    private val binding get() = mBinding!!

    // SharedPreferences
    private val preferenceName = "LOG_IN_DATA"
    private val preferences: SharedPreferences by lazy { getSharedPreferences(preferenceName, Context.MODE_PRIVATE) }
    private val prefEditor: SharedPreferences.Editor by lazy { preferences.edit() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()

        with(binding){
            // 기억하기 체크박스 불러오기
            rememberIdPwdCheckBox.isChecked = preferences.getBoolean("rememberIdPwd",false)

            // 아이디 비밀번호 불러오기
            if(rememberIdPwdCheckBox.isChecked){
                emailET.setText(preferences.getString("email", ""))
                pwdET.setText(preferences.getString("password", ""))
            }

            // for test
            emailET.setText("candy@naver.com")
            pwdET.setText("candy123")
        }

    }


    private fun initListeners(){
        with(binding){
            logo.setOnClickListener{val intent = Intent(applicationContext, MainActivity::class.java)
                // for test
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("userInfo", User(1,"1","1","1",1))
                intent.putExtra("userToken", "userToken")
                startActivity(intent)
                finish()
            }

            // 아이디 비밀번호 기억하기 체크박스
            rememberIdPwdCheckBox.setOnCheckedChangeListener { _, isChecked ->
                prefEditor.putBoolean("rememberIdPwd", isChecked)
                    .commit()

                // 체크 해제한 경우 저장된 정보 지움
                if(!isChecked){
                    prefEditor.putString("email","")
                    prefEditor.putString("password","")
                        .commit()
                }
            }

            findIdBtn.setOnClickListener {
                val intent = Intent(applicationContext, FindIdActivity::class.java)
                startActivity(intent)
            }
            findPwdBtn.setOnClickListener {
                val intent = Intent(applicationContext, FindPasswordActivity::class.java)
                startActivity(intent)
            }
            signupBtn.setOnClickListener {
                val intent = Intent(applicationContext, SignUpActivity::class.java)
                startActivity(intent)
            }
            loginBtn.setOnClickListener {
                logIn()
            }
        }
    }

    private fun logIn() {
        val email = binding.emailET.text.toString()
        val pwd = binding.pwdET.text.toString()

        var userInfo: User
        var userToken: String

        if(email.length in 4..50){

        }

        val reqData = HashMap<String,Any>()
        reqData.put("email",email)
        reqData.put("password",pwd)

        CoroutineScope(Dispatchers.IO).launch{
            RetrofitManager.instance.requestUser(reqData, REQUEST_TYPE.LOG_IN) { responseState, responseBody ->
                when (responseState){
                    RESPONSE_STATE.SUCCESS -> {
                        Log.d(Tag, "api 호출 성공: $responseBody")

                        try {
                            // parse String to Json
                            val result = Gson().fromJson(responseBody, ApiUserResponse::class.java)

                            // 받은 data 저장
                            userInfo = result.response.user
                            userToken = result.response.apiToken

                            // 아이디 비밀번호 저장
                            with(binding){
                                Log.d(Tag,"test")
                                if(rememberIdPwdCheckBox.isChecked){
                                    prefEditor.putString("email",email)
                                    prefEditor.putString("password",pwd)
                                        .commit()
                                }
                            }

                            //  Activity Stack 초기화 후 MainActivity 로 이동
                            val intent = Intent(
                                applicationContext,
                                MainActivity::class.java
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra("userInfo", userInfo)
                            intent.putExtra("userToken", userToken)
                            startActivity(intent)
                            finish()

                        }catch (e: JsonSyntaxException){
                            e.printStackTrace()
                        }
                    }
                    RESPONSE_STATE.FAILURE -> {
                        Log.d(Tag, "api 호출 실패: $responseBody")
                        Util.toast(applicationContext, "로그인에 실패하였습니다")
                    }
                }
            }
        }
    }
}