package com.example.candy.retrofit

import com.example.candy.data.Join
import com.example.candy.data.User
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
//        @Body email: String,
//        @Body password: String,
//        @Body parent_password: String,
//        @Body name: String,
//        @Body phone: String,
//        @Body birth: Any
    ): Call<Join>

}