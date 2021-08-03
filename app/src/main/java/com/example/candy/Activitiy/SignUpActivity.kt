package com.example.candy.Activitiy

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.candy.R
import com.example.candy.Util
import com.example.candy.data.Join
import com.example.candy.databinding.ActivitySignUpBinding
import com.example.candy.retrofit.RetrofitAPI
import com.example.candy.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response


class SignUpActivity : AppCompatActivity() {
    private var mBinding: ActivitySignUpBinding? = null
    private val binding get() = mBinding!!
    private lateinit var gmailCode: String

    private var isEmailDoubleChecked = false
    private var isEmailAuthSended = false
    private var isEmailAuthChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        with(supportActionBar!!){
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "회원가입"
        }

        setListeners()

        //for test
        binding.emailET.setText("test1@naver.com")
        binding.nameET.setText("20010101")
        binding.pwdET.setText("1234")
//        binding.parentPwdET.setText("12345")
        binding.birthET.setText("20000101")
        binding.phoneET.setText("010-1234-4321")
    }

    private fun setListeners(){
        with(binding){
            // 회원가입 버튼. MainActivity로 이동.
            signupBtn.setOnClickListener {
                val api = RetrofitClient.getClient().create(RetrofitAPI::class.java)
                val email = binding.emailET.text
                val pwd = binding.pwdET.text
                val parentPwd = "12345"//binding.parentPwdET.text
                val name = binding.nameET.text
                val phone = binding.phoneET.text
                val birth = binding.birthET.text

                val map = HashMap<String, Any>()
                map["email"] = email
                map["password"] = pwd
                map["parent_password"] = parentPwd
                map["name"] = name
                map["phone"] = phone
                map["birth"] = birth


                CoroutineScope(Dispatchers.IO).launch{
                    val response = api.signUp(map).enqueue(
                        object: retrofit2.Callback<Join> {
                            override fun onResponse(call: Call<Join>, response: Response<Join>) {
                                Log.d("Body:: ",response.body()!!.toString())
                                Log.d("Response:: ",response.body()!!.response.toString())
                                Log.d("User:: ",response.body()!!.response.user.toString())
                            }

                            override fun onFailure(call: Call<Join>, t: Throwable) {
                                Log.d("test  Failure:: ","Failed API call with call")
                            }
                        }
                    )
                    withContext(Dispatchers.Main){
                        // UI
                    }
                }




                //  Activity Stack 초기화 후 MainActivity 로 이동
//                val intent = Intent(applicationContext, MainActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
            }
            // 이메일 중복 확인 버튼.
            emailDoubleCheckBtn.setOnClickListener {
                if(true){   // 중복 확인 성공
                    sendEmailAuthBtn.linksClickable
                    isEmailDoubleChecked = true
                    Util().toast(applicationContext, "사용 가능한 이메일 입니다")
                }else{
                    isEmailDoubleChecked = false
                    Util().toast(applicationContext, "사용 중인 이메일 입니다.")
                }
            }
            // 인증번호 전송 버튼.
            sendEmailAuthBtn.setOnClickListener {
                // 메일 전송.
                if(isEmailDoubleChecked){
                    CoroutineScope(Dispatchers.IO).launch {
                        Util().sendMail(applicationContext, emailET.text.toString())?.let{
                            gmailCode = it
                            isEmailAuthSended = true
                        } ?: run {
//                            Util().toast(applicationContext, "이메일 전송에 실패하였습니다. 다시 시도해주세요")
                        }
                    }
                }else{
                    Util().toast(applicationContext, "이메일 중복을 확인해 주세요")
                }
            }
            // 인증번호 확인 버튼.
            checkEmailAuthBtn.setOnClickListener {
                if(isEmailAuthSended){
                    if(emailAuthET.text.toString() == gmailCode){
                        Util().toast(applicationContext, "이메일 인증에 성공하였습니다")
                        isEmailAuthChecked = true
                    }else{
                        Util().toast(applicationContext, "다시 한번 확인해 주세요")
                        println("${emailAuthET.text}  $gmailCode" )
                    }
                }
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}