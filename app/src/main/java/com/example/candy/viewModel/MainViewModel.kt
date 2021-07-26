package com.example.candy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.candy.data.DataHomeChallengeOngoing

class MainViewModel: ViewModel() {

    val challengeList_uncomplete_Livedata = MutableLiveData<List<DataHomeChallengeOngoing>>()
    val challengeList_complete_Livedata = MutableLiveData<List<DataHomeChallengeOngoing>>()

    private val challengeUncompleteList = arrayListOf<DataHomeChallengeOngoing>()
    private val challengeCompleteList = arrayListOf<DataHomeChallengeOngoing>()

    fun deleteUncomplete(challenge: DataHomeChallengeOngoing){  // 챌린지 완수한 경우

        // 진행 중 챌린지 리스트에서 완수한 챌린지 제거하고 완료한 챌린지 리스트에 추가
        // View에서 라이브데이터를 observe 하고 있는 상태이므로 자동 업데이트 될 것임 (View에서 livedata observe 하는 부분은 아직 구현x)

        challengeUncompleteList.remove(challenge)
        challengeList_uncomplete_Livedata.value = challengeUncompleteList

        challengeCompleteList.add(challenge)
        challengeList_complete_Livedata.value = challengeCompleteList
    }

    /*
        홈 화면의 진행중 챌린지 / 챌린지 화면에서 도전가능&완료  이 세 가지 리스트는 모두 다른 것?


    */
}