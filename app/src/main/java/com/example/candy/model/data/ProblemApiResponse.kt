package com.example.candy.model.data

import com.google.gson.annotations.SerializedName

data class ProblemApiResponse(
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("response") val response: ProblemList,
    @SerializedName("error") val error: Error
)

data class ScoredScoreResponse(
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("response") val response: ScoredScore,
    @SerializedName("error") val error: Error
)

data class ProblemSolveResponse(
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("response") val response: TotalScore,
    @SerializedName("error") val error: Error
)