package com.example.candy.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.candy.model.api.ChallengeApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.data.OnGoingChallenge
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.CurrentUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    var allChallenges1 = ArrayList<OnGoingChallenge>()
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


    fun getOnGoingChallenges(lastChallengeId: Int, size: Int, category: String): LiveData<ArrayList<OnGoingChallenge>> {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                var newChallenges = arrayListOf<OnGoingChallenge>()

                val response = api.getOnGoingChallenges(CurrentUser.userToken!!, lastChallengeId, size)
                if (response.isSuccessful) {
                    // 받아온 챌린지 추가
                    newChallenges = response.body()!!.response
                    newChallenges.forEach {
                        it.category = translateCategory(it.category)
                        allChallenges1.add(it)
                    }
                    Log.d(Tag, "newChallenges / ${newChallenges}")
                }
            }

            // LiveData Update
            sortChallengeByCategory(category)

            Log.d(Tag, "_ongoingChallenges ${_ongoingChallenges.value?.size}/ ${_ongoingChallenges.value?.size} ${_ongoingChallenges.value}")
        }
        return ongoingChallenges
    }

    private fun translateCategory(str: String): String {
        return when (str) {
            "KOREAN" -> "한국어"
            "ENGLISH" -> "영어"
            "MATH" -> "수학"
            else -> "임시"
        }
    }

    fun sortChallengeByCategory(category: String) {
        val newChallenges = ArrayList<OnGoingChallenge>()

        if (category == allCategory) {
            // 전체 카테고리를 클릭 한 경우
            _ongoingChallenges.value = allChallenges1
        } else {
            // 개별 카테고리를 클릭 한 경우
            allChallenges1.forEach {
                if (it.category == category || it.id < 0) {
                    newChallenges.add(it)
                }
            }
            _ongoingChallenges.value = newChallenges
        }
    }

    fun getChallenge(position: Int): OnGoingChallenge {
        return allChallenges1[position]
    }

    fun removeOnGoingChallenge(onGoingChallenge: OnGoingChallenge) {
        Log.d("removeOnGoingChallenge", "before ongoingChallenges / ${ongoingChallenges.value}")
        allChallenges1.remove(onGoingChallenge)
        _ongoingChallenges.value = allChallenges1
        Log.d("removeOnGoingChallenge", "after ongoingChallenges / ${ongoingChallenges.value}")
    }

    fun clearOnGoingChallenges() {
        allChallenges1.clear()
        _ongoingChallenges.value = allChallenges1
    }
}