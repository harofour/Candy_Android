package com.example.candy.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ApiResponse(
    @SerializedName("error")
    @Expose
    val error: Any,
    @SerializedName("response")
    @Expose
    val response: Response,
    @SerializedName("success")
    @Expose
    val success: Boolean
)

