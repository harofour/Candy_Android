package com.example.candy.data

import com.example.candy.model.data.Error
import com.example.candy.model.data.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("apiToken")
    @Expose
    val apiToken: String,
    @SerializedName("user")
    @Expose
    val user: User
)

data class ApiUserResponse(
    @SerializedName("error")
    @Expose
    val error: Error?,
    @SerializedName("response")
    @Expose
    val response: Response,
    @SerializedName("success")
    @Expose
    val success: Boolean
)

data class ApiStringResponse(
    @SerializedName("error")
    @Expose
    val error: Error?,
    @SerializedName("response")
    @Expose
    val response: String,
    @SerializedName("success")
    @Expose
    val success: Boolean
)

data class ApiBooleanResponse(
    @SerializedName("error")
    @Expose
    val error: Error?,
    @SerializedName("response")
    @Expose
    val response: Boolean,
    @SerializedName("success")
    @Expose
    val success: Boolean
)

data class ApiAnyResponse(
    @SerializedName("error")
    @Expose
    val error: Error?,
    @SerializedName("response")
    @Expose
    val response: Any,
    @SerializedName("success")
    @Expose
    val success: Boolean
)

