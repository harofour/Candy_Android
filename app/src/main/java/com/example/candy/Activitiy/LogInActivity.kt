package com.example.candy.Activitiy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.candy.utils.Util
import com.example.candy.data.ApiResponse
import com.example.candy.data.LogInData
import com.example.candy.data.User
import com.example.candy.databinding.ActivityLogInBinding
import com.example.candy.retrofit.IRetrofit
import com.example.candy.retrofit.RetrofitClient
import com.example.candy.retrofit.RetrofitManager
import com.example.candy.utils.RESPONSE_STATE
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response


class LogInActivity : AppCompatActivity() {
    private val Tag: String = "LogInActivity"
    private var mBinding: ActivityLogInBinding? = null
    private val binding get() = mBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
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
//                val api = RetrofitClient.getClient().create(IRetrofit::class.java)
                val email = binding.emailET.text.toString()
                val pwd = binding.pwdET.text.toString()

                if(email.length in 4..50){

                }

                val map = HashMap<String, Any>()
                map["email"] = email
                map["password"] = pwd

                val logInData = LogInData(email, pwd)

                var userInfo: User?

                CoroutineScope(Dispatchers.IO).launch{
                    RetrofitManager.instance.logIn(id = email,password = pwd) { responseState, responseBody ->
                        when (responseState){
                            RESPONSE_STATE.SUCCESS -> {
                                Log.d(Tag, "api 호출 성공: $responseBody")
                            }
                            RESPONSE_STATE.FAILURE -> {
                                Log.d(Tag, "api 호출 실패: $responseBody")
                            }
                        }
                    }


//                    val response = api.logIn(logInData).enqueue(object : retrofit2.Callback<ApiResponse> {
//                        override fun onResponse(
//                            call: Call<ApiResponse>,
//                            response: Response<ApiResponse>
//                        ) {
//
//                            if (response.body()!!.success) {
//
//                                Log.d("Body:: ", response.body()!!.toString())
//                                Log.d("Response:: ", response.body()!!.response.toString())
//                                Log.d("User:: ", response.body()!!.response.user.toString())
//                                userInfo = response.body()!!.response.user
//
//                                //  Activity Stack 초기화 후 MainActivity 로 이동
//                                val intent = Intent(
//                                    applicationContext,
//                                    MainActivity::class.java
//                                )
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                intent.putExtra("userInfo", userInfo)
//                                startActivity(intent)
//                                finish()
//                            } else {
//                                Log.d("Failure:: ", "Log in failed")
//                                Util().toast(applicationContext, "Log in failed")
//                            }
//                        }
//
//                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                            Log.d("Failure:: ", "Failed API call with call")
//                            Util().toast(applicationContext, "Failed API call with call")
//                        }
//                    })
//                    withContext(Dispatchers.Main){
//                        // UI
//                    }
                }
            }
        }
    }
}