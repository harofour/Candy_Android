package com.example.candy.retrofit

import android.util.Log
import com.example.candy.data.ApiResponse
import com.example.candy.data.LogInData
import com.example.candy.data.SignUpData
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.Constants.TAG
import com.example.candy.utils.RESPONSE_STATE
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {

    companion object{
        val instance = RetrofitManager()
    }

    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(BASE_URL).create(IRetrofit::class.java)

    fun logIn(id: String, password: String, completion: (RESPONSE_STATE, String) -> Unit){
        val call = iRetrofit?.logIn(LogInData(id, password)) ?: return

        call.enqueue(object : retrofit2.Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                Log.d(TAG, "RetrofitManager - onResponse() called / response: ${response.body()}")
                completion(RESPONSE_STATE.SUCCESS, response.body().toString())
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAILURE, t.toString())
            }

        })

    }
    fun signUp(data: SignUpData, completion: (RESPONSE_STATE, String) -> Unit){
        val call = iRetrofit?.signUp(data) ?: return

        call.enqueue(object : retrofit2.Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                Log.d(TAG, "RetrofitManager - onResponse() called / response: ${response.body()}")
                completion(RESPONSE_STATE.SUCCESS, response.body().toString())
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAILURE, t.toString())
            }

        })

    }
}