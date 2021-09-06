package com.example.candy.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnGoingChallenge(
    @SerializedName("challengeId") val challengeId: Int,
    @SerializedName("lecturesId") val lecturesId: Int,
    @SerializedName("category") var category: String,
    @SerializedName("title") val title: String,
    @SerializedName("subTitle") val subTitle: String,
    @SerializedName("totalScore") val totalScore: Int,
    @SerializedName("requiredScore") val requiredScore: Int,
    @SerializedName("assignedCandy") val assignedCandy: Int,
    @SerializedName("level") val level: Int,
    @SerializedName("complete") val complete: Boolean = false
) : Parcelable

// 진행 중인 챌린지 이므로 우선 완수 여부는 false