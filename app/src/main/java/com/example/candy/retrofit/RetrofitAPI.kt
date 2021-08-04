package com.example.candy.retrofit

import com.example.candy.data.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitAPI {
    // 로그인
//    @POST("/user/login")
//    fun logIn(@Query("userId") id: String): Call<User>
    // 회원가입
    @POST("/user/join")
    fun signUp(
        @Body map: HashMap<String,Any>
    ): Call<ApiResponse>


    @POST("/auth/authenticate")
    fun logIn(
        @Body map: HashMap<String,Any>
    ): Call<ApiResponse>

}