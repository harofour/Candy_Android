package com.example.candy.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.candy.model.data.OnGoingChallenge

class HomeViewModel() : ViewModel() {
    companion object {
        private var instance: HomeViewModel? = null
        fun getInstance() = instance ?: synchronized(HomeViewModel::class.java) {
            instance ?: HomeViewModel().also { instance = it }
        }
    }

    private val repository = HomeRepository()

    fun getCategories(): LiveData<ArrayList<String>> {
        return repository.getCategories()
    }

    fun getOnGoingChallengeLiveData():  LiveData<ArrayList<OnGoingChallenge>>{
        return repository.getOnGoingChallengeLiveData()
    }

    fun clearLiveData(){
        repository.clearLiveData()
    }

    fun loadData(lastChallengeId: Int, size: Int, category: String){
        return repository.loadData(lastChallengeId, size, category)
    }

    fun getChallenge(position: Int): OnGoingChallenge {
        return repository.getChallenge(position)
    }

    fun getLastChallengeId():Int{
        return repository.getLastChallengeId()
    }

    fun removeOnGoingChallenge(onGoingChallenge: OnGoingChallenge) {
        repository.removeOnGoingChallenge(onGoingChallenge)
    }

    fun clearOnGoingChallenges(){
        repository.clearOnGoingChallenges()
    }
}