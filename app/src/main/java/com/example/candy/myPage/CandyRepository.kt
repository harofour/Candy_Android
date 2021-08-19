package com.example.candy.myPage

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

class CandyRepository(application: Application) {
    val TAG: String = "로그"
    private val retrofit = RetrofitClient.getClient(BASE_URL)
    private val api = retrofit.create(CandyApi::class.java)


    fun getUserInfo() : User {
        return CurrentUser.userInfo!!
    }

    fun getAPICandyStudent(apiKey: String){
        api.getCandyStudent(apiKey).enqueue(object : Callback<CandyResponse> {
            override fun onResponse(call: Call<CandyResponse>, response: Response<CandyResponse>) {
                CurrentUser.studentCandy.value = response.body()!!.candy
            }

            override fun onFailure(call: Call<CandyResponse>, t: Throwable) {
                Log.d(TAG, "Repository - onFailure() called")
            }
        })
    }

    fun getAPICandyParent(apiKey: String){
        api.getCandyParent(apiKey).enqueue(object : Callback<CandyResponse>{
            override fun onResponse(call: Call<CandyResponse>, response: Response<CandyResponse>) {
                CurrentUser.parentCandy.value = response.body()!!.candy
            }

            override fun onFailure(call: Call<CandyResponse>, t: Throwable) {
                Log.d(TAG, "Repository - onFailure() called")
            }
        })
    }

    fun getCandyStudent() : LiveData<Candy>{
        return CurrentUser.studentCandy
    }

    fun getCandyParent(): LiveData<Candy> {
        return CurrentUser.parentCandy
    }

    fun updateCandyParent(apiKey: String, chargeCandy: HashMap<String,Int>) {
        api.chargeCandy(apiKey,chargeCandy).enqueue(object : Callback<chargeCandyResponse>{
            override fun onResponse(
                call: Call<chargeCandyResponse>,
                response: Response<chargeCandyResponse>
            ) {
                if(response.code() == 200 && response.body()!!.success){
                    CurrentUser.parentCandy.value = Candy(response.body()!!.response.candy)
                }
            }

            override fun onFailure(call: Call<chargeCandyResponse>, t: Throwable) {

            }
        })
    }
}