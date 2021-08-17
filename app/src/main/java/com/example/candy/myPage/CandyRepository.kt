package com.example.candy.myPage

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.candy.data.Candy
import com.example.candy.data.CandyResponse
import com.example.candy.data.User
import com.example.candy.retrofit.IRetrofit
import com.example.candy.utils.CurrentUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CandyRepository(application: Application) {
    val TAG: String = "로그"
    private val retrofit = CandyAPI.getClient()
    private val api = retrofit.create(IRetrofit::class.java)


    fun getUserInfo() : User {
        return CurrentUser.userInfo!!
    }

    fun getAPICandyStudent(){
        Log.d(TAG, "Repository - getCandyStudent() called")

        api.getCandyStudent().enqueue(object : Callback<CandyResponse> {
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