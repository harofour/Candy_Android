package com.example.candy.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnGoingChallenge(
        val id: Int,
        var category: String,
        val title: String,
        val subTitle: String,
        val totalScore: Int,
        val requiredScore: Int,
        val assignedCandy: Int,
        val complete: Boolean = false
): Parcelable




// 진행 중인 챌린지 이므로 우선 완수 여부는 false