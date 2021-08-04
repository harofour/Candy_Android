package com.example.candy.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("birth")
    @Expose
    val birth: String,
    @SerializedName("loginCount")
    @Expose
    val loginCount: Int,
    @SerializedName("authority")
    @Expose
    val authority: Any
):Serializable