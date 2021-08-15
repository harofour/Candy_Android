package com.example.candy.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

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

