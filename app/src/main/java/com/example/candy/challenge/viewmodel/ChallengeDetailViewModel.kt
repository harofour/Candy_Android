package com.example.candy.challenge.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
     val assignedCandy = MutableLiveData<Int>()

    private var challengeDetail: ChallengeDetail? = null

    val challengeDetailProgressbar = MutableLiveData<Boolean>()

    val candyAmount = MutableLiveData<Int>()
    val dialogProgressBarVisible = MutableLiveData<Boolean>()

    val assignSuccess = MutableLiveData<Boolean>()



    fun getChallengeDetailInfo(challengeId: Int){
        viewModelScope.launch {
            challengeDetailProgressbar.postValue(true)
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
                assignedCandy.postValue(challengeDetail!!.assignedCandy)
            }
            challengeDetailProgressbar.postValue(false)
        }
    }


    @SuppressLint("CheckResult")
    fun getParentCandy(){
        dialogProgressBarVisible.postValue(true)
        challengeRepository.getParentCandyAmount(CurrentUser.userToken!!).subscribe(
            { it ->
                candyAmount.postValue(it.candy.candy)
                Log.d("api test", "get parent candy success!")
                dialogProgressBarVisible.postValue(false)
            }
            ,{throwable -> Log.d("api test","get parent candy error!")
                dialogProgressBarVisible.postValue(false)
            }
        )
    }

    @SuppressLint("CheckResult")
    fun assignCandy(challengeId: Int, candyCnt: Int){
        dialogProgressBarVisible.postValue(true)
        challengeRepository.assignCandy(CurrentUser.userToken!!, challengeId, candyCnt).subscribe(
            {
                Log.d("api test", "candy assign success!")
                assignSuccess.postValue(it.isAssignSuccess)
            }
            , {
                throwable -> Log.d("api test", "candy assign error!")
            }
        )

    }








}