package com.example.candy.retrofit

import android.util.Log
import com.example.candy.data.ApiResponse
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.Constants.TAG
import com.example.candy.utils.RESPONSE_STATE
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {

    companion object{
        val instance = RetrofitManager()
    }

    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(BASE_URL).create(IRetrofit::class.java)

    fun logIn(data: HashMap<String,Any>, completion: (RESPONSE_STATE, String) -> Unit){
        val call = iRetrofit?.logIn(data) ?: return

        call.enqueue(object : retrofit2.Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                Log.d(TAG, "RetrofitManager - onResponse() called / response.body(): ${response.body()}")
                Log.d(TAG, "RetrofitManager - onResponse() called / response.code(): ${response.code()}")

                if(response.code() == 200){
                    // 성공
                    val str = Gson().toJson(response.body(), ApiResponse::class.java).toString()
                    completion(RESPONSE_STATE.SUCCESS, str)
                }else{
                    // 실패
                    completion(RESPONSE_STATE.FAILURE, response.code().toString())
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAILURE, t.toString())
            }
        })

    }

    fun signUp(data: HashMap<String,Any>, completion: (RESPONSE_STATE, String) -> Unit){
        val call = iRetrofit?.signUp(data) ?: return

        call.enqueue(object : retrofit2.Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                Log.d(TAG, "RetrofitManager - onResponse() called / response.body(): ${response.body()}")
                Log.d(TAG, "RetrofitManager - onResponse() called / response.code(): ${response.code()}")
                if(response.code() == 200){
                    // 성공
                    completion(RESPONSE_STATE.SUCCESS, response.body().toString())
                }else{
                    // 실패
                    completion(RESPONSE_STATE.FAILURE, response.code().toString())
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAILURE, t.toString())
            }
        })
    }

    fun verifyEmail(data: HashMap<String,Any>, completion: (RESPONSE_STATE, String) -> Unit){
        val call = iRetrofit?.verifyEmail(data) ?: return

        call.enqueue(object : retrofit2.Callback<ApiResponse>{
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                Log.d(TAG, "RetrofitManager - onResponse() called / response.body(): ${response.body()}")
                Log.d(TAG, "RetrofitManager - onResponse() called / response.code(): ${response.code()}")
                if(response.code() == 200){
                    // 성공
                    completion(RESPONSE_STATE.SUCCESS, response.body().toString())
                }else{
                    // 실패
                    completion(RESPONSE_STATE.FAILURE, response.code().toString())
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAILURE, t.toString())
            }
        })

    }
}