package com.example.candy.model.api

import com.example.candy.model.data.ProblemApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ProblemApi {

    @POST("challenge/problem/return")
    fun getProblem(
        @Header("api_key")userToken: String,
        @Body challengeID: HashMap<String,Int>
    ): Call<ProblemApiResponse>
}