package com.example.candy.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CandyResponse(
    @SerializedName("response")
    val candy: Candy
)

data class Candy(
    @SerializedName("candyAmount")
    val candy: String
)

data class chargeCandyResponse(
    @SerializedName("error")
    @Expose
    val error: Error?,
    @SerializedName("response")
    @Expose
    val response: Candy,
    @SerializedName("success")
    @Expose
    val success: Boolean
)

