package com.example.candy.model.api

import com.example.candy.data.ApiAnyResponse
import com.example.candy.model.data.CandyResponse
import com.example.candy.model.data.chargeCandyResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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
    ) : Call<chargeCandyResponse>

    // 캔디 획득
    @POST("candy/attain")
    fun attainCandy(
        @Header("api_key")userToken: String,
        @Body attainCandy: HashMap<String,Int>
    ) : Call<ApiAnyResponse>
}