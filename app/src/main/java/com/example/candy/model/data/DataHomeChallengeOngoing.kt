package com.example.candy.model.data

data class DataHomeChallengeOngoing(
        val category: String,
        val title: String,
        val description: String,
        val assigned_candy: Int,
        val required_score: Int,
        val complete: Boolean = false
)




// 진행 중인 챌린지 이므로 우선 완수 여부는 false