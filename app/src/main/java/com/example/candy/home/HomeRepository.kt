package com.example.candy.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.candy.model.api.CandyApi
import com.example.candy.model.api.ChallengeApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.data.OnGoingChallenge
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.CurrentUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class HomeRepository() {
    private var isLoading: Boolean = false
    private val Tag = "HomeRepository"
    private val retrofit = RetrofitClient.getClient(BASE_URL)
    private val challengeApi = retrofit.create(ChallengeApi::class.java)
    private val candyApi = retrofit.create(CandyApi::class.java)

    private val _categories: MutableLiveData<ArrayList<String>> =
        MutableLiveData<ArrayList<String>>()
    private var categories: LiveData<ArrayList<String>> = _categories

    private val _ongoingChallenges: MutableLiveData<ArrayList<OnGoingChallenge>> =
        MutableLiveData<ArrayList<OnGoingChallenge>>()
    private var ongoingChallenges: LiveData<ArrayList<OnGoingChallenge>> = _ongoingChallenges

    private val allCategories = ArrayList<String>()
    private val allCategory = "전체"
    private var allChallenges = ArrayList<OnGoingChallenge>()
    private val lastChallengeId: Int
        get() {
            return if (allChallenges.isEmpty()) {
                100000
            } else {
                if (allChallenges.last().challengeId < 0) {   // 마지막 Item 이 로딩 뷰 인 경우
                    if (allChallenges.size == 1)
                        100000
                    else
                        allChallenges[allChallenges.lastIndex - 1].challengeId
                } else {
                    allChallenges.last().challengeId
                }
            }
        }


    /**
     * 카테고리 request
     */
    fun getCategories(): LiveData<ArrayList<String>> {
        CoroutineScope(Dispatchers.IO).launch {
            val response = challengeApi.getCategory(CurrentUser.userToken!!)

            if (allCategories.size > 0) {
                allCategories.clear()
            }

            allCategories.add(allCategory)

            response.body()?.let {
                it.forEach { category ->
                    // 영어인 카테고리를 한글로 변경하여 저장
                    allCategories.add(translateCategory(category))
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

    /**
     * return LiveData
     */
    fun getOnGoingChallengeLiveData(): LiveData<ArrayList<OnGoingChallenge>> {
        return ongoingChallenges
    }

    /**
     * 카테고리 클릭 시 데이터를 초기화 하고 다시 불러옴
     */
    fun clearLiveData() {
        allChallenges.clear()
        _ongoingChallenges.value = allChallenges
    }

    /**
     * 서버에 진행중 챌린지 리스트 Request.
     * 리스트를 받아와 카테고리 한글로 변경,
     * 리스트 마지막에 로딩 뷰가 들어있다면 삭제 후 전체 리스트에 추가,
     * 카테고리로 정렬하여 LiveData 에 업데이트
     * 서버로부터 응답이 오기 전에 사용 불가
     */
    fun loadData(size: Int, category: String) {
        if (isLoading) {
            return
        }
        Log.d("Tag", "lastChallengeId / $lastChallengeId ")
        isLoading = true
        var newChallenges = arrayListOf<OnGoingChallenge>()
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                val response =
                    challengeApi.getOnGoingChallenges(
                        CurrentUser.userToken!!,
                        lastChallengeId,
                        size
                    )
                if (response.isSuccessful) {
                    // 받아온 챌린지 리스트
                    newChallenges = response.body()!!.response

                    // 카테고리 이름 한글로
                    newChallenges.forEach {
                        it.category = translateCategory(it.category)
                    }
                }
            }

            // deleted loading view
            if (allChallenges.isNotEmpty() && newChallenges.isNotEmpty())
                if (allChallenges.last().challengeId < 0)
                    allChallenges.removeLast()

            // add response to all challenge list
            newChallenges.forEach { item ->
                allChallenges.add(item)
            }

            // sort all challenge list and update LiveData
            val reloadSize = sortChallengeByCategory(category, size)

            // TODO : 정렬 후 개수가 적으면 데이터 더 요청
//            if(reloadSize > 2){
//                loadData(reloadSize, category)
//            }

            isLoading = false
        }
    }

    /**
     * 카테고리 이름이 영어라 한글로 변경
     */
    private fun translateCategory(str: String): String {
        return when (str) {
            "KOREAN" -> "한국어"
            "ENGLISH" -> "영어"
            "MATH" -> "수학"
            else -> "??"
        }
    }

    /**
     * 카테고리별 정렬하여 업데이트
     * return : 정렬 후의 리스트 개수가 작을 시 데이터를 더 요청하기 위한 Size
     */
    private fun sortChallengeByCategory(category: String, size: Int): Int {
        val sortedChallenges = ArrayList<OnGoingChallenge>()

        if (category == allCategory) {
            // 전체 카테고리를 클릭 한 경우
            _ongoingChallenges.value = allChallenges
        } else {
            // 개별 카테고리를 클릭 한 경우
            allChallenges.forEach {
                if (it.category == category || it.challengeId < 0) {
                    sortedChallenges.add(it)
                }
            }
            _ongoingChallenges.value = sortedChallenges
        }

        if (allCategories.size < size && sortedChallenges.size < size) {
            return size - sortedChallenges.size
        }
        return 0
    }

    /**
     * 챌린지 클릭 시 강의 페이지로 넘겨줄 챌린지 반환
     */
    fun getChallenge(position: Int): OnGoingChallenge {
        return allChallenges[position]
    }

    /**
     * 챌린지 완료, 캔디 배정 취소 시 리스트에서 제거
     */
    fun removeOnGoingChallenge(onGoingChallenge: OnGoingChallenge) {
        allChallenges.remove(onGoingChallenge)
        _ongoingChallenges.value = allChallenges
    }

    /**
     * 캔디 배정 취소
     */
    suspend fun cancelAssignedCandy(reqData: HashMap<String, Any>): Boolean = withContext(
        CoroutineScope(Dispatchers.IO).coroutineContext
    ) {
        val response = candyApi.cancelCandy(CurrentUser.userToken!!, reqData)
        Log.d("cancelAssignedCandy", "${response.body()}")
        response.isSuccessful
    }
}