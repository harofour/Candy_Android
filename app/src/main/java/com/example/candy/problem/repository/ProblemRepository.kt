package com.example.candy.problem.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.candy.model.api.CandyApi
import com.example.candy.model.api.ProblemApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.data.ProblemApiResponse
import com.example.candy.model.data.ProblemList
import com.example.candy.utils.API.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProblemRepository() {
    private val retrofit = RetrofitClient.getClient(BASE_URL)
    private val api = retrofit.create(ProblemApi::class.java)

    var problemList = MutableLiveData<ProblemList>()


    fun getProblem(apiKey: String, challengeID: HashMap<String, Int>) : LiveData<ProblemList>{
        val data = MutableLiveData<ProblemList>()
        api.getProblem(apiKey,challengeID).enqueue(object : Callback<ProblemApiResponse> {
            override fun onResponse(
                call: Call<ProblemApiResponse>,
                response: Response<ProblemApiResponse>
            ) {
                val TAG: String = "ProblemRepository"
                if (response.code() == 200) {
                    Log.d(TAG, "${response.body()!!.response.problem.size}")
                    data.value = response.body()!!.response
                }
            }

            override fun onFailure(call: Call<ProblemApiResponse>, t: Throwable) {

            }
        })

        return data
    }
}