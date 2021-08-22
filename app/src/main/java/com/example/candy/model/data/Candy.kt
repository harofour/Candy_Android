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
    val error: Error?,
    @SerializedName("response")
    val response: Candy,
    @SerializedName("success")
    val success: Boolean
)

