package com.example.candy.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.candy.model.api.ChallengeApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.data.Challenge
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

    private val _ongoingChallenges: MutableLiveData<ArrayList<Challenge>> =
        MutableLiveData<ArrayList<Challenge>>()
    private var ongoingChallenges: LiveData<ArrayList<Challenge>> = _ongoingChallenges

    private val allCategories = ArrayList<String>()
    private val allCategory = "전체"
    var allChallenges = ArrayList<Challenge>()

    fun getCategories(): LiveData<ArrayList<String>> {
        CoroutineScope(Dispatchers.IO).launch {
            val request = api.getCategory(CurrentUser.userToken!!)

            if (allCategories.size > 0) {
                allCategories.clear()
            }

            allCategories.add(allCategory)

            request.body()?.let {
                it.forEach { category ->
                    allCategories.add(translateCategory(category))
//                    allCategories.add(category)
                }
            }

            if (request.isSuccessful) {
                _categories.postValue(allCategories)
            } else {
                Log.d(Tag, "getCategories() error occurred")
            }
        }
        return categories
    }


    fun getOnGoingChallenges(): LiveData<ArrayList<Challenge>> {
        allChallenges = generateChallengeData()
        _ongoingChallenges.postValue(allChallenges)
        return ongoingChallenges
    }

    // for test
    private fun generateChallengeData(): ArrayList<Challenge> {
        return arrayListOf<Challenge>(
            Challenge(1, "영어", false, 1, 1, "1형식", "ㄱ"),
            Challenge(2, "수학", false, 1, 1, "덧셈", "ㄴ"),
            Challenge(3, "한국어", false, 1, 1, "말하기", "ㄷ"),
            Challenge(4, "영어", false, 1, 1, "3형식", "ㄹ"),
            Challenge(5, "수학", false, 1, 1, "곱하기", "ㅁ"),
            Challenge(6, "영어", false, 1, 1, "5형식", "ㅂ")
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
        val newChallenges = ArrayList<Challenge>()
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

    fun getChallenge(position: Int): Challenge {
        return allChallenges[position]
    }
}