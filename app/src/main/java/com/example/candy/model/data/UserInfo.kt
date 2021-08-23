package com.example.candy.model.data

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("error")
    val error: Error?,
    @SerializedName("response")
    val response: UserInfo,
    @SerializedName("success")
    val success: Boolean
)

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