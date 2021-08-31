package com.example.candy.model.api

import com.example.candy.data.ApiAnyResponse
import com.example.candy.model.data.CandyResponse
import com.example.candy.model.data.ChargeCandyResponse
import com.example.candy.model.data.HistoryResponse
import retrofit2.Call
import retrofit2.http.*

interface CandyApi {

    // 학생캔디 조회
    @GET("candy/student")
    fun getCandyStudent(
        @Header("api_key")userToken: String
    ) : Call<CandyResponse>

    // 학부모캔디 조회
    @GET("candy/parent")
    fun getCandyParent(
        @Header("api_key")userToken: String
    ) : Call<CandyResponse>

    // 캔디 충전
    @POST("candy/charge")
    fun chargeCandy(
        @Header("api_key")userToken: String,
        @Body chargeCandy: HashMap<String,Int>
    ) : Call<ChargeCandyResponse>

    // 캔디 인출
    @POST("candy/withdraw")
    fun withdrawCandy(
        @Header("api_key")userToken: String,
        @Body chargeCandy: HashMap<String,Int>
    ) : Call<ChargeCandyResponse>

    // 캔디 내역 조회
    @GET("/candy/history/{identity}/{category}/{lastCandyHistoryId}/{size}")
    fun getCandyHistory(
        @Header("api_key")userToken: String,
        @Path("identity")identity: String,
        @Path("category")category: String,
        @Path("lastCandyHistoryId")lastCandyHistoryId: String,
        @Path("size")size: String
    ) : Call<HistoryResponse>
    // 캔디 획득
    @POST("candy/attain")
    fun attainCandy(
        @Header("api_key")userToken: String,
        @Body attainCandy: HashMap<String,Int>
    ) : Call<ApiAnyResponse>
}