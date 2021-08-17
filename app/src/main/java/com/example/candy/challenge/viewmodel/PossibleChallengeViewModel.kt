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

    val possibleChallengeLiveData = MutableLiveData<List<Challenge>>()
    private var possibleChallengeDataList : List<Challenge>? = null// 외부에서 수정 불가

    var progressVisible = MutableLiveData<Boolean>() // progressbar

    fun getAllPossibleChallengeList(){
        viewModelScope.launch {

            progressVisible.postValue(true)
            possibleChallengeDataList = challengeRepository.searchPossibleChallenge(CurrentUser.userToken!!)
            Log.d("api test", "getAllPossibleChallengeList 호출")
            if(possibleChallengeDataList != null){
                possibleChallengeLiveData.value = possibleChallengeDataList!!
                progressVisible.postValue(false)
            }
        }
    }





}