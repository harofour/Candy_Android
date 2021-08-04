package com.example.candy.Activitiy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.candy.Util
import com.example.candy.data.ApiResponse
import com.example.candy.data.User
import com.example.candy.databinding.ActivityLogInBinding
import com.example.candy.retrofit.RetrofitAPI
import com.example.candy.retrofit.RetrofitClient
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response


class LogInActivity : AppCompatActivity() {
    private val TAG: String = "LogInActivity"
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

                val api = RetrofitClient.getClient().create(RetrofitAPI::class.java)
                val email = binding.emailET.text
                val pwd = binding.pwdET.text

                val map = HashMap<String, Any>()
                map["email"] = email
                map["password"] = pwd

                Log.d(TAG, "email : $email // password : $pwd")

                var userInfo: User?

                CoroutineScope(Dispatchers.IO).launch{
                    val response = api.logIn(map).enqueue(
                        object: retrofit2.Callback<ApiResponse> {
                            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                Log.d("Body:: ",response.body()!!.toString())
                                Log.d("Response:: ",response.body()!!.response.toString())
                                Log.d("User:: ",response.body()!!.response.user.toString())
                                userInfo = response.body()!!.response.user

                                if(response.body()!!.success){
                                    //  Activity Stack 초기화 후 MainActivity 로 이동
                                    val intent = Intent(applicationContext, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.putExtra("userInfo",userInfo)
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Log.d("Failure:: ","Log in failed")
                                    Util().toast(applicationContext, "Log in failed")
                                }
                            }

                            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                Log.d("Failure:: ","Failed API call with call")
                                Util().toast(applicationContext, "Failed API call with call")
                            }
                        }
                    )
                    withContext(Dispatchers.Main){
                        // UI
                    }
                }
            }
        }
    }
}