package com.example.candy.challenge.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.candy.home.HomeRepository
import com.example.candy.model.data.Challenge
import com.example.candy.model.data.ChallengeComplete
import com.example.candy.model.repository.ChallengeListRepository
import com.example.candy.utils.CurrentUser
import kotlinx.coroutines.launch

class CompleteChallengeViewModel(
        private val challengeRepository: ChallengeListRepository
): ViewModel() {

    private val homeRepository = HomeRepository()

    val completeChallengeLiveData = MutableLiveData<ArrayList<ChallengeComplete>>()
    private var completeChallengeDataList : ArrayList<ChallengeComplete>? = null// 외부에서 수정 불가

    private var totalCompleteList: Int = 0
    val totalCompleteListLiveData = MutableLiveData<Int>()

    var progressVisible = MutableLiveData<Boolean>() // progressbar


    fun getAllCompleteChallengeList(lastChallengeId: Int, size: Int, initial: Boolean, category: String){
        viewModelScope.launch {

            if(initial == true){
                progressVisible.postValue(true)
                totalCompleteList = 0
                totalCompleteListLiveData.postValue(totalCompleteList)
            }
            completeChallengeDataList = challengeRepository.searchCompleteChallenge(
                    CurrentUser.userToken!!, lastChallengeId, size)
            Log.d("api test check", "호출 시 사용한 챌린지 id : ${lastChallengeId}")
            Log.d("api test check", "getAllCompleteChallengeList 호출")
            Log.d("api test check","찜 리스트 response list 수 : ${completeChallengeDataList?.size}")
            if(completeChallengeDataList != null){

                if(completeChallengeDataList!!.size > 0){
                    if(category == "전체")
                        completeChallengeLiveData.value = completeChallengeDataList!!
                    else{
                        var completeChallengeDataListbyCategory : ArrayList<ChallengeComplete> = ArrayList()
                        completeChallengeDataList!!.forEach{ it ->
                            var transCategory =  translateCategory(it.category)
                            if(transCategory == category)
                                completeChallengeDataListbyCategory.add(it)
                        }
                        completeChallengeLiveData.value = completeChallengeDataListbyCategory
                    }
                }
                else {
                    completeChallengeLiveData.value = completeChallengeDataList!!
                }

                progressVisible.postValue(false)

                totalCompleteList += completeChallengeDataList!!.size
                totalCompleteListLiveData.postValue(totalCompleteList)
                Log.d("api test check", "현재 완료 챌린지 개수 : ${totalCompleteList}")
            }
            else{
                Log.d("api test check","reponse 빈 리스트 반환됨")
            }

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