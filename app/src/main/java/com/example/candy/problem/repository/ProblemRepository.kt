package com.example.candy.problem.repository

import android.app.Application
import com.example.candy.model.api.CandyApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.utils.API.BASE_URL

class ProblemRepository(application: Application) {
    private val retrofit = RetrofitClient.getClient(BASE_URL)
    private val api = retrofit.create(CandyApi::class.java)
}