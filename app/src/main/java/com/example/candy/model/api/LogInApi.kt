package com.example.candy.model.api

import com.example.candy.data.ApiBooleanResponse
import com.example.candy.data.ApiUserResponse
import com.example.candy.data.ApiStringResponse
import com.example.candy.model.data.CandyResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LogInApi {
    // 로그인
    @POST("auth/authenticate")
    fun logIn(@Body logInData: HashMap<String,Any>): Call<ApiUserResponse>

    // 회원가입
    @POST("user/join")
    fun signUp(@Body sighUpData: HashMap<String,Any>): Call<ApiUserResponse>

    // 이메일 중복 확인
    @POST("user/email/exist")
    fun verifyEmail(@Body emailData: HashMap<String,Any>): Call<ApiBooleanResponse>

    // 이메일 찾기
    @POST("user/find_email")
    fun findEmail(@Body emailData: HashMap<String,Any>): Call<ApiStringResponse>

    // 이메일 인증
    @POST("user/email")
    fun sendAuth(@Body emailData: HashMap<String,Any>): Call<ApiBooleanResponse>

    // 이메일 인증 코드 확인
    @POST("user/email/validate")
    fun checkAuth(@Body emailData: HashMap<String,Any>): Call<ApiBooleanResponse>

    // 비밀번호 재설정
    @POST("user/new_pw")
    fun resetPassword(@Body emailData: HashMap<String,Any>): Call<ApiBooleanResponse>

}