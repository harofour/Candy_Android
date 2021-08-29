package com.example.candy.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.candy.model.api.ChallengeApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.data.Challenge
import com.example.candy.model.data.OnGoingChallenge
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.CurrentUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeRepository() {
    private val Tag = "HomeRepository"
    private val retrofit = RetrofitClient.getClient(BASE_URL)
    private val api = retrofit.create(ChallengeApi::class.java)

    private val _categories: MutableLiveData<ArrayList<String>> =
        MutableLiveData<ArrayList<String>>()
    private var categories: LiveData<ArrayList<String>> = _categories

    private val _ongoingChallenges: MutableLiveData<ArrayList<OnGoingChallenge>> =
        MutableLiveData<ArrayList<OnGoingChallenge>>()
    private var ongoingChallenges: LiveData<ArrayList<OnGoingChallenge>> = _ongoingChallenges

    private val allCategories = ArrayList<String>()
    private val allCategory = "전체"
    var allChallenges = ArrayList<OnGoingChallenge>()

    fun getCategories(): LiveData<ArrayList<String>> {
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getCategory(CurrentUser.userToken!!)

            if (allCategories.size > 0) {
                allCategories.clear()
            }

            allCategories.add(allCategory)

            response.body()?.let {
                it.forEach { category ->
                    allCategories.add(translateCategory(category))
//                    allCategories.add(category)
                }
            }

            if (response.isSuccessful) {
                _categories.postValue(allCategories)
            } else {
                Log.d(Tag, "getCategories() error occurred")
            }
        }
        return categories
    }


    fun getOnGoingChallenges(): LiveData<ArrayList<OnGoingChallenge>> {
        CoroutineScope(Dispatchers.Main).launch{
            CoroutineScope(Dispatchers.IO).async {
                val response = api.getOnGoingChallenges(CurrentUser.userToken!!, 1000000, 10000)
                if(response.isSuccessful){
                    allChallenges = response.body()!!.response
                    allChallenges.forEach { it.category = translateCategory(it.category) }
                    Log.d(Tag,"${allChallenges}")
                }
            }.await()
            _ongoingChallenges.value = allChallenges
        }

//        allChallenges = generateChallengeData()
        return ongoingChallenges
    }

    // for test
    private fun generateChallengeData(): ArrayList<OnGoingChallenge> {
        return arrayListOf<OnGoingChallenge>(
//            OnGoingChallenge("영어", "제목1","설명1",50,80,false),
//            OnGoingChallenge("영어", "제목1","설명1",50,80,false),
//            OnGoingChallenge("영어", "제목1","설명1",50,80,false),
//            OnGoingChallenge("영어", "제목1","설명1",50,80,false),
//            OnGoingChallenge("영어", "제목1","설명1",50,80,false),
//            OnGoingChallenge("영어", "제목1","설명1",50,80,false)
        )
    }

    private fun translateCategory(str: String): String {
        return when (str) {
            "KOREAN" -> "한국어"
            "ENGLISH" -> "영어"
            "MATH" -> "수학"
            else -> "임시"
        }
    }

    fun sortChallengeByCategory(position: Int) {
        val newChallenges = ArrayList<OnGoingChallenge>()
        val selectedCategory = allCategories[position]

        if (selectedCategory == allCategory) {
            // 전체 카테고리를 클릭 한 경우
            _ongoingChallenges.value = allChallenges
        } else {
            // 개별 카테고리를 클릭 한 경우
            allChallenges.forEach {
                if (it.category == selectedCategory) {
                    newChallenges.add(it)
                }
            }
            _ongoingChallenges.value = newChallenges
        }
    }

    fun getChallenge(position: Int): OnGoingChallenge {
        return allChallenges[position]
    }
}