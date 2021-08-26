package com.example.candy.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.candy.model.api.ChallengeApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.CurrentUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeRepository() {
    private val Tag = "HomeRepository"
    private val retrofit = RetrofitClient.getClient(BASE_URL)
    private val api = retrofit.create(ChallengeApi::class.java)

    private val _categories: MutableLiveData<ArrayList<String>> =
        MutableLiveData<ArrayList<String>>()
    private var categories: LiveData<ArrayList<String>> = _categories

    fun getCategories(): LiveData<ArrayList<String>> {
        CoroutineScope(Dispatchers.IO).launch {
            val request = api.getCategory(CurrentUser.userToken!!)
            val data = ArrayList<String>()

            request.body()?.let {
                it.forEach { category ->
                    data.add(translateCategory(category))
                }
            }

            if (request.isSuccessful) {
                _categories.postValue(data)
            } else {
                Log.d(Tag, "getCategories() error occurred")
            }
        }
        return categories
    }

    private fun translateCategory(str: String): String {
        return when (str) {
            "KOREAN" -> "한국어"
            "ENGLISH" -> "영어"
            "MATH" -> "수학"
            else -> "임시"
        }
    }


}