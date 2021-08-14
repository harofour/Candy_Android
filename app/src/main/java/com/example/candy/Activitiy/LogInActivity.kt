package com.example.candy.Activitiy

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.candy.data.ApiResponse
import com.example.candy.data.User
import com.example.candy.databinding.ActivityLogInBinding
import com.example.candy.retrofit.RetrofitManager
import com.example.candy.utils.RESPONSE_STATE
import com.example.candy.utils.Util
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit
import java.util.logging.Handler
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.timer


class LogInActivity : BaseActivity() {
    private val Tag: String = "LogInActivity"
    private var mBinding: ActivityLogInBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()

        with(binding){
            // 아이디 비밀번호 불러오기
            if(rememberIdPwdCheckBox.isChecked){
                emailET.setText("")
                pwdET.setText("")
            }
        }

        // for test
        with(binding){
            emailET.setText("hi@naver.com")
            pwdET.setText("rhdahwjs12")
        }
    }


    private fun setListeners(){
        with(binding){
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

//                //  Activity Stack 초기화 후 MainActivity 로 이동
//                val intent = Intent(
//                    applicationContext,
//                    MainActivity::class.java
//                )
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                intent.putExtra("userInfo", User("1","1",1,"1",1,"1"))
//                startActivity(intent)
//                finish()



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
                    RetrofitManager.instance.logIn(reqData) { responseState, responseBody ->
                        when (responseState){
                            RESPONSE_STATE.SUCCESS -> {
                                Log.d(Tag, "api 호출 성공: $responseBody")

                                // String to Gson
                                try {
                                    // parse String to Json
                                    val result = Gson().fromJson(responseBody, ApiResponse::class.java)

                                    // 받은 data 저장
                                    userInfo = result.response.user
                                    userToken = result.response.apiToken

//                                    // 아이디 비밀번호 저장
//                                    with(binding){
//                                        if(rememberIdPwdCheckBox.isChecked){
//
//                                        }
//                                    }

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
                            }
                        }
                    }
                }
            }
        }
    }
}