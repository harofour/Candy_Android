package com.example.candy.myPage

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.candy.model.api.CandyApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.data.Candy
import com.example.candy.model.data.CandyResponse
import com.example.candy.model.data.User
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
        Log.d(TAG, "Repository - getCandyStudent() called")

        api.getCandyStudent(apiKey).enqueue(object : Callback<CandyResponse> {
            override fun onResponse(call: Call<CandyResponse>, response: Response<CandyResponse>) {
                CurrentUser.userCandy.value = response.body()!!.candy
            }

            override fun onFailure(call: Call<CandyResponse>, t: Throwable) {
                Log.d(TAG, "Repository - onFailure() called")
            }
        })
    }

    fun getCandyStudent() : LiveData<Candy>{
        return CurrentUser.userCandy
    }
}