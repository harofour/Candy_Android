package com.example.candy.challenge.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.candy.home.HomeRepository
import com.example.candy.model.data.Challenge
import com.example.candy.model.repository.ChallengeListRepository
import com.example.candy.utils.CurrentUser
import kotlinx.coroutines.launch

class LikeChallengeViewModel(
    private val challengeRepository: ChallengeListRepository
): ViewModel() {

    private val homeRepository = HomeRepository()

    val likeChallengeLiveData = MutableLiveData<ArrayList<Challenge>>()
    private var likeChallengeDataList : ArrayList<Challenge>? = null// 외부에서 수정 불가

    private var totalLikeList: Int = 0
    val totalLikeListLiveData = MutableLiveData<Int>()


    var progressVisible = MutableLiveData<Boolean>() // progressbar
    var emptyTextVisible = MutableLiveData<Boolean>()  // 찜 목록 0개 인 경우 텍스트 뷰 visible


    fun getAllLikeChallengeList(lastChallengeId: Int, size: Int, initial: Boolean, category: String){
        viewModelScope.launch {

            if(initial == true){
                progressVisible.postValue(true)
                totalLikeList = 0
                totalLikeListLiveData.postValue(totalLikeList)
            }
            likeChallengeDataList = challengeRepository.searchLikeChallenge(
                    CurrentUser.userToken!!, lastChallengeId, size)
            Log.d("api test check", "호출 시 사용한 챌린지 id : ${lastChallengeId}")
            Log.d("api test check", "getAllLikeChallengeList 호출")
            Log.d("api test check","찜 리스트 response list 수 : ${likeChallengeDataList?.size}")
            if(likeChallengeDataList != null){

                if(likeChallengeDataList!!.size > 0){
                    if(category == "전체")
                        likeChallengeLiveData.value = likeChallengeDataList!!
                    else{
                        var likeChallengeDataListbyCategory : ArrayList<Challenge> = ArrayList()
                        likeChallengeDataList!!.forEach{ it ->
                            var transCategory =  translateCategory(it.category)
                            if(transCategory == category)
                                likeChallengeDataListbyCategory.add(it)
                        }
                        likeChallengeLiveData.value = likeChallengeDataListbyCategory
                    }
                }
                else {
                    likeChallengeLiveData.value = likeChallengeDataList!!
                }

                progressVisible.postValue(false)

                totalLikeList += likeChallengeDataList!!.size
                totalLikeListLiveData.postValue(totalLikeList)
                Log.d("api test check", "현재 찜 개수 : ${totalLikeList}")
            }
            else{
                Log.d("api test check","reponse 빈 리스트 반환됨")
            }

        }


    }

    fun touchLikeImage(challengeId: Int, previousState: Boolean){
        viewModelScope.launch {
            progressVisible.postValue(true)
            var response = challengeRepository.touchLikeBtn(CurrentUser.userToken!!, challengeId, previousState)
            progressVisible.postValue(false)

            totalLikeList -= 1
            totalLikeListLiveData.postValue(totalLikeList)
            Log.d("api test check", "현재 찜 개수 : ${totalLikeList}")
        }
    }


    fun getCategories(): LiveData<ArrayList<String>> {
        return homeRepository.getCategories()
    }


    private fun translateCategory(str: String): String {
        return when (str) {
            "KOREAN" -> "한국어"
            "ENGLISH" -> "영어"
            "MATH" -> "수학"
            else -> "??"
        }
    }

}