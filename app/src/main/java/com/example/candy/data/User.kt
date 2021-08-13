package com.example.candy.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("birth")
    @Expose
    val birth: String,
    @SerializedName("loginCount")
    @Expose
    val loginCount: Int
):Serializable