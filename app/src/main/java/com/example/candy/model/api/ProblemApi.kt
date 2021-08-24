package com.example.candy.model.api

import com.example.candy.model.data.ProblemApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ProblemApi {

    @GET("challenge/problem/solve")
    fun getProblem(
        @Header("api_key")userToken: String
    ): Call<ProblemApiResponse>
}