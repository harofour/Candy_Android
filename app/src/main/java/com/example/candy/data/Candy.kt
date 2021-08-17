package com.example.candy.data

import com.google.gson.annotations.SerializedName

data class CandyResponse(
    @SerializedName("response")
    val candy: Candy
)

data class Candy(
    @SerializedName("candyAmount")
    val candy: String
)
