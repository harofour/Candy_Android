package com.example.candy.challenge.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.candy.model.data.ChallengeDetail
import com.example.candy.model.repository.ChallengeListRepository
import com.example.candy.utils.CurrentUser
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch

class ChallengeDetailViewModel(
        private val challengeRepository: ChallengeListRepository
): ViewModel() {

     val title = MutableLiveData<String>()
     val subTitle = MutableLiveData<String>()
     val category = MutableLiveData<String>()
     val description = MutableLiveData<String>()
     val level = MutableLiveData<Int>()
     val likeDone = MutableLiveData<Boolean>()
     val requiredScore = MutableLiveData<Int>()
     val totalScore =  MutableLiveData<Int>()

    private var challengeDetail: ChallengeDetail? = null


    fun getChallengeDetailInfo(challengeId: Int){
        viewModelScope.launch {

            Log.d("api test", "ChallengeDetailViewModel getChallengeDetailInfo called")

            challengeDetail = challengeRepository.searchChallengeDetail(CurrentUser.userToken!!, challengeId)

            if(challengeDetail != null){
                title.postValue(challengeDetail!!.title)
                subTitle.postValue(challengeDetail!!.subTitle)
                category.postValue(challengeDetail!!.category)
                description.postValue(challengeDetail!!.description)
                level.postValue(challengeDetail!!.level)
                likeDone.postValue(challengeDetail!!.likeDone)
                requiredScore.postValue(challengeDetail!!.requiredScore)
                totalScore.postValue(challengeDetail!!.totalScore)
            }
        }
    }



}