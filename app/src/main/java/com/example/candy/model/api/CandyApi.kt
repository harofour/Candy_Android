package com.example.candy.model.api

import com.example.candy.model.data.CandyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CandyApi {

    // 학생캔디 조회
    @GET("candy/student")
    fun getCandyStudent(
        @Header("api_key")userToken: String
    ) : Call<CandyResponse>
}