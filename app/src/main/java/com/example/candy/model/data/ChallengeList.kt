package com.example.candy.model.data

import com.google.gson.annotations.SerializedName

data class ChallengeList(
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("response") val response: ArrayList<Challenge>
)
