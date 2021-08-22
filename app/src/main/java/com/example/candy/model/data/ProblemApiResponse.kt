package com.example.candy.model.data

import com.google.gson.annotations.SerializedName

data class ProblemApiResponse(
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("response") val response: ArrayList<Problem>
)