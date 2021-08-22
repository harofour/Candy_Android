package com.example.candy.model.api

import com.example.candy.R
import com.example.candy.model.data.ChallengeLike
import com.example.candy.model.data.ChallengeList
import com.example.candy.utils.CurrentUser
import retrofit2.Response
import retrofit2.http.*

interface ChallengeApi {

    // 도전 가능 챌린지 리스트 조회 (캔디 배정x, 진행중x)
    @GET("challenge/possibleList")
    suspend fun getPossibleChallengeList(
        @Header("api_key")userToken: String,
        @Query("lastChallengeId")lastChallengeId: Int,
        @Query("size")size: Int
    ): Response<ChallengeList>

    @POST("challenge/{challengeId}/like")
    suspend fun touchLikeBtn(
            @Header("api_key")userToken: String,
            @Path("challengeId")challengeId: Int
    ): Response<ChallengeLike>

}