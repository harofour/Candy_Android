package com.example.candy.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.candy.R
import com.example.candy.data.ApiBooleanResponse
import com.example.candy.data.ApiUserResponse
import com.example.candy.data.Response
import com.example.candy.utils.Util
import com.example.candy.model.data.User
import com.example.candy.databinding.ActivitySignUpBinding
import com.example.candy.retrofit.RetrofitManager
import com.example.candy.utils.REQUEST_TYPE
import com.example.candy.utils.RESPONSE_STATE
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignUpActivity : BaseActivity() {
    private val Tag = "SignUpActivity"
    private var mBinding: ActivitySignUpBinding? = null
    private val binding get() = mBinding!!
    private lateinit var gmailCode: String

    private var isEmailVerified = false         // 이메일 중복 확인
    private var isEmailAuthSended = false       // 이메일 전송 확인
    private var isEmailAuthChecked = false      // 이메일 인증코드 확인

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

        initListeners()

        //for test
        binding.emailET.setText("candy@naver.com")
        binding.nameET.setText("candy")
        binding.pwdET.setText("candy123")
        binding.pwdCheckET.setText("candy123")
        binding.parentPwdET.setText("candy123")
        binding.parentPwdCheckET.setText("candy123")
        binding.birthET.setText("2000-01-01")
        binding.phoneET.setText("010-1234-4321")
    }

    private fun initListeners(){
        with(binding){
            // 이메일 중복 확인 버튼.
            verifyEmailButton.setOnClickListener {
                verifyEmail()
            }
            // 인증번호 전송 버튼.
            sendEmailAuthBtn.setOnClickListener {
                sendAuth()
            }
            // 인증번호 확인 버튼.
            checkEmailAuthBtn.setOnClickListener {
                checkAuth()
            }
            // 회원가입 버튼.
            signupBtn.setOnClickListener {
                signUp()
            }
        }
    }

    private fun verifyEmail(){
        val email = binding.emailET.text.toString()
        val reqData = HashMap<String,Any>()
        reqData.put("email",email)

        CoroutineScope(Dispatchers.IO).launch{
            RetrofitManager.instance.requestBoolean(reqData, REQUEST_TYPE.VERIFY_EMAIL){ responseState, responseBody ->
                when(responseState){
                    RESPONSE_STATE.SUCCESS -> {
                        // String to Gson
                        val result = Gson().fromJson(responseBody, ApiBooleanResponse::class.java)
                        isEmailVerified = !result.response  // true -> 중복 , false -> 사용가능

                        if(isEmailVerified)
                            Util.toast(applicationContext, "사용 가능한 아이디 입니다")
                        else
                            Util.toast(applicationContext, "아이디가 중복되었습니다")
                    }
                    RESPONSE_STATE.FAILURE -> {
                        Util.toast(applicationContext, "아이디가 중복되었습니다")
                        isEmailVerified = false
                    }
                }
            }
        }
    }

    private fun sendAuth(){
        if(isEmailAuthSended){
            Util.toast(applicationContext, "이미 전송되었습니다")
            Log.d(Tag,"인증 메일이 이미 전송됨 / isEmailAuthSended : $isEmailAuthSended")
            return
        }

        val data = HashMap<String,Any>()
        data.put("email", binding.emailET.text.toString())

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
                    isEmailAuthSended = false
                    Util.toast(applicationContext, "존재하지 않는 아이디 입니다2")
                }
            }
        }
    }

    private fun checkAuth(){
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

        val data = HashMap<String,Any>()
        data.put("email", binding.emailET.text.toString())
        data.put("auth", binding.emailAuthET.text.toString())

        RetrofitManager.instance.requestBoolean(data, REQUEST_TYPE.CHECK_AUTH){ responseSTate, responseBody ->
            when(responseSTate){
                RESPONSE_STATE.SUCCESS -> { // 인증 코드 확인 결과
                    val result = Gson().fromJson(responseBody, ApiBooleanResponse::class.java)
                    isEmailAuthChecked = result.response
                    if(result.response){
                        Util.toast(applicationContext, "인증이 완료되었습니다.")
                    }else{
                        Util.toast(applicationContext, "인증 코드를 확인해 주세요")
                    }
                }
                RESPONSE_STATE.FAILURE -> {
                    isEmailAuthChecked = false
                    Util.toast(applicationContext, "인증 코드를 확인해 주세요2")
                }
            }
        }
    }

    private fun signUp() {
        val email = binding.emailET.text.toString()
        val pwd = binding.pwdET.text.toString()
        val pwdCheck = binding.pwdCheckET.text.toString()
        val parentPwd = binding.parentPwdET.text.toString()
        val parentPwdCheck = binding.parentPwdCheckET.text.toString()
        val name = binding.nameET.text.toString()
        val phone = binding.phoneET.text.toString()
        val birth = binding.birthET.text.toString()

        // 회원가입 정보 조건 체크
        if(!isEmailVerified) {
            Util.toast(applicationContext, "이메일 중복을 확인해 주세요")
            Log.d(Tag, "이메일 중복 확인 x")
            return
        }
//        if(!isEmailAuthChecked) {
//            Util.toast(applicationContext, "이메일을 인증해 주세요")
//            Log.d(Tag, "이메일 인증 x")
//            return
//        }
        if(pwd != pwdCheck) {
            Log.d(Tag, "parent 가 다름")
            Util.toast(applicationContext, "비밀번호가 다릅니다.")
            return
        }
        if(parentPwd != parentPwdCheck) {
            Log.d(Tag, "parent password 가 다름")
            Util.toast(applicationContext, "2차 비밀번호가 다릅니다")
            return
        }
//                if(email.length !in 4..50){
//                    Log.d(Tag, "id 길이가 범위를 벗어남.")
//                    Util.toast(applicationContext, "아이디는 4~50자사이로 입력해 주세요")
//                }else if(pwd.length !in 8..20){
//                    Log.d(Tag, "password 길이가 범위를 벗어남.")
//                    Util.toast(applicationContext, "비밀번호를 8~20자사이로 입력해 주세요")
//                }else if(parentPwd.length !in 8..20) {
//                    Log.d(Tag, "parent password 길이가 범위를 벗어남")
//                    Util.toast(applicationContext, "2차 비밀번호는 8~20자 사이로 입력해 주세요")
//                }


        // 서버에 전송할 데이터
        val reqData = HashMap<String,Any>()
        reqData.put("email",email)
        reqData.put("emailCheck",true)
        reqData.put("password", pwd)
        reqData.put("parentPassword", parentPwd)
        reqData.put("name", name)
        reqData.put("phone", phone)
        reqData.put("birth", birth)

        var userInfo: User     // 서버로부터 받아올 유저 정보
        var userToken: String

        // 서버 통신
        CoroutineScope(Dispatchers.IO).launch{
            RetrofitManager.instance.requestUser(reqData, REQUEST_TYPE.SIGN_UP){ responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.SUCCESS -> {
                        Log.d(Tag, "api 호출 성공: $responseBody")

                        // String to Gson
                        val result = Gson().fromJson(responseBody, ApiUserResponse::class.java)

                        // 받은 User 객체 저장
                        with(result.response as Response){
                            userInfo = user
                            userToken = apiToken
                        }

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
                    }
                    RESPONSE_STATE.FAILURE -> {
                        Log.d(Tag, "api 호출 실패: $responseBody")
                    }
                }
            }
        }
    }
}
