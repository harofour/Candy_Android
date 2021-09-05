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

class PossibleChallengeViewModel(
    private val challengeRepository: ChallengeListRepository
): ViewModel() {

    private val homeRepository = HomeRepository()

    val possibleChallengeLiveData = MutableLiveData<ArrayList<Challenge>>()
    private var possibleChallengeDataList : ArrayList<Challenge>? = null// 외부에서 수정 불가

    var progressVisible = MutableLiveData<Boolean>() // progressbar

    fun getAllPossibleChallengeList(lastChallengeId: Int, size: Int, initial: Boolean, category: String){
        viewModelScope.launch {

            if(initial == true){
                progressVisible.postValue(true)
            }
            possibleChallengeDataList = challengeRepository.searchPossibleChallenge(
                    CurrentUser.userToken!!, lastChallengeId, size)
            Log.d("api test check", "getAllPossibleChallengeList 호출")
            Log.d("api test check","도전가능 리스트 response list 수 : ${possibleChallengeDataList?.size}")
            if(possibleChallengeDataList != null){

                if(possibleChallengeDataList!!.size > 0){
                    if(category == "전체")
                        possibleChallengeLiveData.value = possibleChallengeDataList!!
                    else{
                        var possibleChallengeDataListbyCategory : ArrayList<Challenge> = ArrayList()
                        possibleChallengeDataList!!.forEach{it ->
                            var transCategory =  translateCategory(it.category)
                            if(transCategory == category)
                                possibleChallengeDataListbyCategory.add(it)
                        }
                        possibleChallengeLiveData.value = possibleChallengeDataListbyCategory
                    }
                }
                else
                    possibleChallengeLiveData.value = possibleChallengeDataList!!

                progressVisible.postValue(false)
            }
            else{
                Log.d("api test","reponse 빈 리스트 반환됨")
            }

        }
    }

    fun touchLikeImage(challengeId: Int, previousState: Boolean){
        viewModelScope.launch {
            progressVisible.postValue(true)
            var response = challengeRepository.touchLikeBtn(CurrentUser.userToken!!, challengeId, previousState)
            progressVisible.postValue(false)


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