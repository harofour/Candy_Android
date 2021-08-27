package com.example.candy.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Challenge(
    @SerializedName("id") val challengeId: Int,
    @SerializedName("category") val category: String,
    @SerializedName("likeDone") var likeDone: Boolean,
    @SerializedName("requiredScore") val requiredScore: Int,
    @SerializedName("totalScore") val totalScore: Int,
    @SerializedName("title") val title: String,
    @SerializedName("subTitle") val subTitle: String
): Parcelable
