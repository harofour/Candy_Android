package com.example.candy.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.candy.R
import com.example.candy.data.ApiResponse
import com.example.candy.utils.Util
import com.example.candy.data.User
import com.example.candy.databinding.ActivitySignUpBinding
import com.example.candy.retrofit.IRetrofit
import com.example.candy.retrofit.RetrofitClient
import com.example.candy.retrofit.RetrofitManager
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.RESPONSE_STATE
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class SignUpActivity : AppCompatActivity() {
    private val Tag = "SignUpActivity"
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
        binding.pwdET.setText("12341234")
        binding.parentPwdET.setText("12345123")
        binding.birthET.setText("2000-01-01")
        binding.phoneET.setText("010-1234-4321")
    }

    private fun setListeners(){
        with(binding){
            // 회원가입 버튼. MainActivity로 이동.
            signupBtn.setOnClickListener {
                val api = RetrofitClient.getClient(BASE_URL).create(IRetrofit::class.java)

                val email = binding.emailET.text.toString()
                val pwd = binding.pwdET.text.toString()
                val pwdCheck = binding.pwdCheckET.text.toString()
                val parentPwd = binding.parentPwdET.text.toString()
                val parentPwdCheck = binding.parentPwdCheckET.text.toString()
                val name = binding.nameET.text.toString()
                val phone = binding.phoneET.text.toString()
                val birth = binding.birthET.text.toString()

                val map = HashMap<String, Any>()
                map["email"] = email
                map["password"] = pwd
                map["parent_password"] = parentPwd
                map["name"] = name
                map["phone"] = phone
                map["birth"] = birth


//                if(email.length !in 4..50){
//                    Log.d(Tag, "id 길이가 범위를 벗어남.")
//                    Util.toast(applicationContext, "아이디는 4~50자사이로 입력해 주세요")
//                }else if(pwd.length !in 8..20){
//                    Log.d(Tag, "password 길이가 범위를 벗어남.")
//                    Util.toast(applicationContext, "비밀번호를 8~20자사이로 입력해 주세요")
//                }else if(parentPwd.length !in 8..20) {
//                    Log.d(Tag, "parent password 길이가 범위를 벗어남")
//                    Util.toast(applicationContext, "2차 비밀번호는 8~20자 사이로 입력해 주세요")
//                }else if(pwd != pwdCheck) {
//                    Log.d(Tag, "parent 가 다름")
//                    Util.toast(applicationContext, "비밀번호가 다릅니다.")
//                }else if(parentPwd != parentPwdCheck) {
//                    Log.d(Tag, "parent password 가 다름")
//                    Util.toast(applicationContext, "2차 비밀번호가 다릅니다")
//                }else{
                if(true){
                    // 서버에 전송할 데이터
                    val reqData = HashMap<String,Any>()
                    reqData.put("email",email)
                    reqData.put("emailCheck",true)
                    reqData.put("password", pwd)
                    reqData.put("parent_password", parentPwd)
                    reqData.put("name", name)
                    reqData.put("phone", phone)
                    reqData.put("birth", birth)

                    var userInfo: User?     // 서버로부터 받아온 유저 정보


                    if(isEmailDoubleChecked && isEmailAuthChecked){
                        if(isEmailAuthChecked){
                            // 서버 통신
                            CoroutineScope(Dispatchers.IO).launch{
                                RetrofitManager.instance.signUp(reqData){ responseState, responseBody ->
                                    when (responseState) {
                                        RESPONSE_STATE.SUCCESS -> {
                                            Log.d(Tag, "api 호출 성공: $responseBody")

                                            // String to Gson
                                            val result = Gson().fromJson(responseBody, ApiResponse::class.java)

                                            // 받은 User 객체 저장
                                            userInfo = result.response.user

                                            val intent = Intent(
                                                applicationContext,
                                                MainActivity::class.java
                                            )
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            intent.putExtra("userInfo", userInfo)
                                            startActivity(intent)
                                            finish()
                                        }
                                        RESPONSE_STATE.FAILURE -> {
                                            Log.d(Tag, "api 호출 실패: $responseBody")
                                        }
                                    }
                                }
                            }
                        }else{
                            Util.toast(applicationContext, "이메일을 인증해 주세요")
                        }
                    }else{
                        Util.toast(applicationContext, "이메일 중복을 확인해 주세요")
                    }
                }

            }
            // 이메일 중복 확인 버튼.
            emailDoubleCheckBtn.setOnClickListener {
                if(true){   // 중복 확인 성공
                    sendEmailAuthBtn.linksClickable
                    isEmailDoubleChecked = true
                    Util.toast(applicationContext, "사용 가능한 이메일 입니다")
                    emailDoubleCheckBtn.isClickable = false
                }else {
                    isEmailDoubleChecked = false
                    Util.toast(applicationContext, "사용 중인 이메일 입니다.")
                }
            }
            // 인증번호 전송 버튼.
            sendEmailAuthBtn.setOnClickListener {
                // 메일 전송.
                if(!isEmailAuthSended){
                    if(isEmailDoubleChecked){
                        CoroutineScope(Dispatchers.IO).launch {
                            Util.sendMail(applicationContext, emailET.text.toString())?.let{
                                gmailCode = it
                                isEmailAuthSended = true
                            }
                        }
                        sendEmailAuthBtn.isClickable = false

                    }else{
                        Util.toast(applicationContext, "이메일 중복을 확인해 주세요")
                    }
                }else{
                    Util.toast(applicationContext, "인증 메일이 이미 전송되었습니다")
                }
            }
            // 인증번호 확인 버튼.
            checkEmailAuthBtn.setOnClickListener {
                if(gmailCode == emailAuthET.text.toString()){
                    sendEmailAuthBtn.linksClickable
                    isEmailAuthChecked = true
                    Util.toast(applicationContext, "이메일 인증이 완료되었습니다")
                    checkEmailAuthBtn.isClickable = false
                }else {
                    Util.toast(applicationContext, "잘못된 인증 코드 입니다.")
                    println("${emailAuthET.text} != $gmailCode")
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