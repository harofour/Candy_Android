package com.example.candy.data

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