package com.example.candy.model.api

import com.example.candy.data.ApiStringResponse
import com.example.candy.model.data.UserChangePwResponse
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

    // 마이페이지에서 비밀번호 변경
    @POST("user/password/change")
    fun changePw(
        @Header("api_key")userToken: String,
        @Body UserData: HashMap<String,Any>
    ) : Call<UserChangePwResponse>

    // 마이페이지에서 2차 비밀번호 변경
    @POST("user/parent/password/change")
    fun changeParentPw(
        @Header("api_key")userToken: String,
        @Body UserData: HashMap<String,Any>
    ) : Call<UserChangePwResponse>
}