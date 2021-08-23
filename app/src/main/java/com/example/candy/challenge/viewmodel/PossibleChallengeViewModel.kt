package com.example.candy.challenge.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.candy.model.data.Challenge
import com.example.candy.model.repository.ChallengeListRepository
import com.example.candy.utils.CurrentUser
import kotlinx.coroutines.launch

class PossibleChallengeViewModel(
    private val challengeRepository: ChallengeListRepository
): ViewModel() {

    val possibleChallengeLiveData = MutableLiveData<ArrayList<Challenge>>()
    private var possibleChallengeDataList : ArrayList<Challenge>? = null// 외부에서 수정 불가

    var progressVisible = MutableLiveData<Boolean>() // progressbar

    fun getAllPossibleChallengeList(lastChallengeId: Int, size: Int, initial: Boolean){
        viewModelScope.launch {

            if(initial == true){
                progressVisible.postValue(true)
            }
            possibleChallengeDataList = challengeRepository.searchPossibleChallenge(
                    CurrentUser.userToken!!, lastChallengeId, size)
            Log.d("api test", "getAllPossibleChallengeList 호출")
            Log.d("api test","도전가능 리스트 response list 수 : ${possibleChallengeDataList?.size}")
            if(possibleChallengeDataList != null){
                possibleChallengeLiveData.value = possibleChallengeDataList!!
                progressVisible.postValue(false)
            }
            else{
                Log.d("api test","reponse 빈 리스트 반환됨")
            }

        }
    }





}