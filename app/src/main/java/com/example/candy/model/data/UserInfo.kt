package com.example.candy.model.data

import com.google.gson.annotations.SerializedName

// 유저 정보 변경 관련
data class UserInfoResponse(
    @SerializedName("error")
    val error: Error?,
    @SerializedName("response")
    val response: UserInfo,
    @SerializedName("success")
    val success: Boolean
)

// 유저 정보 변경 관련
data class UserInfo(
    @SerializedName("birth")
    var birth: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    var phone: String
)

// 유저 패스워드 관련
data class UserChangePwResponse(
    @SerializedName("error")
    val error: Error?,
    @SerializedName("response")
    val response: UserId,
    @SerializedName("success")
    val success: Boolean
)

// 유저 패스워드 관련
data class UserId(
    @SerializedName("userId")
    val userId : Int
)