package com.example.candy.model.api

import com.example.candy.model.data.UserInfoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserInfoApi {

    // 유저 정보 얻기
    @GET("user/info")
    fun getUserInfo(
        @Header("api_key")userToken: String,
    ) : Call<UserInfoResponse>

    // 유저 정보 변경
    @POST("user/info/change")
    fun updateUserInfo(
        @Header("api_key")userToken: String,
        @Body UserData: HashMap<String,Any>
    ) : Call<UserInfoResponse>
}