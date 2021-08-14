package com.example.candy.retrofit

import com.example.candy.data.ApiResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IRetrofit {
    // 로그인
    @POST("auth/authenticate")
    fun logIn(@Body logInData: HashMap<String,Any>): Call<ApiResponse>

    // 회원가입
    @POST("user/join")
    fun signUp(@Body sighUpData: HashMap<String,Any>): Call<ApiResponse>

    // 이메일 중복 확인
    @POST("user/email/exist")
    fun verifyEmail(@Body emailData: HashMap<String,Any>): Call<ApiResponse>

    // 이메일 찾기
    @POST("user/find_email")
    fun findEmail(@Body emailData: HashMap<String,Any>): Call<ApiResponse>

    // 비밀번호 재설정
    @POST("user/new_pw")
    fun resetPassword(@Body emailData: HashMap<String,Any>): Call<ApiResponse>
}