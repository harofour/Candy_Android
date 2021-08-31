package com.example.candy.model.api

import com.example.candy.R
import com.example.candy.model.data.*
import com.example.candy.utils.CurrentUser
import io.reactivex.Single
import retrofit2.Call
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

    // 찜하기
    @POST("challenge/{challengeId}/like")
    suspend fun touchLikeBtn(
            @Header("api_key")userToken: String,
            @Path("challengeId")challengeId: Int
    ): Response<ChallengeLike>

    // 찜한 리스트 조회
    @GET("challenge/likeList")
    suspend fun getLikeChallengeList(
            @Header("api_key")userToken: String,
            @Query("lastChallengeId")lastChallengeId: Int,
            @Query("size")size: Int
    ): Response<ChallengeList>

    // 소개 정보 받아오기
    @GET("challenge/{challengeId}/detail")
    suspend fun getChallengeDetail(
            @Header("api_key")userToken: String,
            @Path("challengeId")challengeId: Int
    ): Response<ChallengeDetailResponse>


    // 카테고리 조회
    @GET("challenge/category")
    suspend fun getCategory(
        @Header("api_key")userToken: String
    ): Response<ArrayList<String>>


    // 학부모캔디 조회 rx
    @GET("candy/parent")
    fun getParentCandyAmount(
        @Header("api_key")userToken: String
    ) : Single<CandyResponse2>


    // 캔디 배정 rx
    @POST("candy/assign")
    fun assignCandy(
        @Header("api_key")userToken: String,
        @Body candyAssignData: CandyAssignBody
    ) : Single<CandyAssignResponse>



    @GET("challenge/notCompletedList")
    suspend fun getOnGoingChallenges(
        @Header("api_key")userToken: String,
        @Query("lastChallengeId")lastChallengeId: Int,
        @Query("size")size: Int
    ): Response<OnGoingChallengeResponse>

}