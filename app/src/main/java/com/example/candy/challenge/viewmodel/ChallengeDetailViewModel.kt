package com.example.candy.challenge.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.candy.model.data.ChallengeDetail
import com.example.candy.model.repository.ChallengeListRepository
import com.example.candy.utils.CurrentUser
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
     val assignedCandyCount = MutableLiveData<Int>()

    private var challengeDetail: ChallengeDetail? = null

    val challengeDetailProgressbar = MutableLiveData<Boolean>()
    val challengeDetailVideoLoadProgressbar = MutableLiveData<Boolean>()

    val candyAmount = MutableLiveData<Int>()
    val dialogProgressBarVisible = MutableLiveData<Boolean>()

    val loadVideoSuccess = MutableLiveData<Boolean>()
    val videoUrl = MutableLiveData<String>()

    val assignSuccess = MutableLiveData<Boolean>()

    val pw2Error = MutableLiveData<Boolean>() // 2차 비번 틀리는 경우 메시지 알림용



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
                assignedCandyCount.postValue(challengeDetail!!.assignedCandy)
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
    fun assignCandy(challengeId: Int, candyCnt: Int, parrentPassword: String){
        dialogProgressBarVisible.postValue(true)
        challengeRepository.assignCandy(CurrentUser.userToken!!, challengeId, candyCnt, parrentPassword).subscribe(
            {
                Log.d("api test check", "candy assign success!")
                assignSuccess.postValue(it.isAssignSuccess)
                dialogProgressBarVisible.postValue(false)
            }
            , {
                throwable -> Log.d("api test check", "candy assign error! : " + throwable.message)
                dialogProgressBarVisible.postValue(false)
                pw2Error.postValue(true)
            }
        )

    }


    // 영상 url 가져오기
    @SuppressLint("CheckResult")
    fun getVideoUrl(challengeId: Int, lectureId: Int){
        challengeDetailVideoLoadProgressbar.postValue(true)
        challengeRepository.loadVideo(CurrentUser.userToken!!, challengeId, lectureId).subscribe(
                {
                  Log.d("api test check", "load video success!!")
                    videoUrl.postValue(it.lecturesUrl.get(0))
                    challengeDetailVideoLoadProgressbar.postValue(false)
                    Log.d("api test check", "video url : ${it.lecturesUrl.get(0)}")
                }
                ,{
                    throwable -> Log.d("api test check", "load video fail!! : " + throwable.message)
                    challengeDetailVideoLoadProgressbar.postValue(false)
                }
        )

    }





}