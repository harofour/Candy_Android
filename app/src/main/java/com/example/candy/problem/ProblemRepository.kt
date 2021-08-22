package com.example.candy.problem

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.candy.model.api.CandyApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.data.Candy
import com.example.candy.model.data.CandyResponse
import com.example.candy.model.data.User
import com.example.candy.model.data.chargeCandyResponse
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.CurrentUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProblemRepository(application: Application) {
    val TAG: String = "로그"
    private val retrofit = RetrofitClient.getClient(BASE_URL)
    private val api = retrofit.create(CandyApi::class.java)


}