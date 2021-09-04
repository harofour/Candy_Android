package com.example.candy.model.api

import com.example.candy.model.data.ProblemApiResponse
import com.example.candy.model.data.ScoredScoreResponse
import retrofit2.Call
import retrofit2.http.*

interface ProblemApi {

    @POST("challenge/problem/return")
    fun getProblem(
        @Header("api_key")userToken: String,
        @Body challengeID: HashMap<String,Int>
    ): Call<ProblemApiResponse>

    // 챌린지 현재 점수 받아오는 API
    @GET("challenge/score/{challengeId}")
    fun getScoredScore(
        @Header("api_key")userToken: String,
        @Path("challengeId")challengeId : Int
    ): Call<ScoredScoreResponse>
}