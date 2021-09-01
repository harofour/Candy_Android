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
    private var isLoading: Boolean = false
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
                    // 영어인 카테고리를 한글로 변경.
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

    fun getOnGoingChallengeLiveData(): LiveData<ArrayList<OnGoingChallenge>>{
        return ongoingChallenges
    }

    fun clearLiveData(){
        allChallenges.clear()
        _ongoingChallenges.value = allChallenges
    }

    fun loadData(lastChallengeId: Int, size: Int, category: String){
        if(isLoading){
            return
        }
        isLoading = true
        var newChallenges = arrayListOf<OnGoingChallenge>()
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                val response = api.getOnGoingChallenges(CurrentUser.userToken!!, lastChallengeId, size)
                if (response.isSuccessful) {
                    // 받아온 챌린지 리스트
                    newChallenges = response.body()!!.response

                    // 카테고리 이름 한글로
                    newChallenges.forEach {
                        it.category = translateCategory(it.category)
                    }
                    Log.d(Tag, "newChallenges / ${newChallenges}")
                }
            }
            // deleted loading view
            if(allChallenges.isNotEmpty())
                if(allChallenges.last().id < 0)
                    allChallenges.removeLast()

            // add response to all challenge list
            newChallenges.forEach { item ->
                allChallenges.add(item)
            }

            // sort all challenge list and update LiveData
            sortChallengeByCategory(category)

            isLoading = false
        }
    }

    fun getLastChallengeId(): Int{
        return allChallenges.run{
            if(this.isNotEmpty())
                allChallenges.last().id
            else
                -1
        }
    }

    private fun translateCategory(str: String): String {
        return when (str) {
            "KOREAN" -> "한국어"
            "ENGLISH" -> "영어"
            "MATH" -> "수학"
            else -> "임시"
        }
    }

    private fun sortChallengeByCategory(category: String) {
        val sortedChallenges = ArrayList<OnGoingChallenge>()

        if (category == allCategory) {
            // 전체 카테고리를 클릭 한 경우
            _ongoingChallenges.value = allChallenges
        } else {
            // 개별 카테고리를 클릭 한 경우
            allChallenges.forEach {
                if (it.category == category || it.id < 0) {
                    sortedChallenges.add(it)
                }
            }
            _ongoingChallenges.value = sortedChallenges
        }
        Log.d(Tag, "_ongoingChallenges ${_ongoingChallenges.value?.size}/ ${_ongoingChallenges.value?.size} ${_ongoingChallenges.value}")
    }

    fun getChallenge(position: Int): OnGoingChallenge {
        return allChallenges[position]
    }

    fun removeOnGoingChallenge(onGoingChallenge: OnGoingChallenge) {
        Log.d("removeOnGoingChallenge", "before ongoingChallenges / ${ongoingChallenges.value}")
        allChallenges.remove(onGoingChallenge)
        _ongoingChallenges.value = allChallenges
        Log.d("removeOnGoingChallenge", "after ongoingChallenges / ${ongoingChallenges.value}")
    }

    fun clearOnGoingChallenges() {
        allChallenges.clear()
        _ongoingChallenges.value = allChallenges
    }
}