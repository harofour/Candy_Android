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

    fun getOnGoingChallenges(lastChallengeId: Int, size: Int, category: String): LiveData<ArrayList<OnGoingChallenge>> {
        return repository.getOnGoingChallenges(lastChallengeId, size, category)
    }

    fun sortChallengeByCategory(category: String) {
        repository.sortChallengeByCategory(category)
    }

    fun getChallenge(position: Int): OnGoingChallenge {
        return repository.getChallenge(position)
    }

    fun removeOnGoingChallenge(onGoingChallenge: OnGoingChallenge) {
        repository.removeOnGoingChallenge(onGoingChallenge)
    }

    fun clearOnGoingChallenges(){
        repository.clearOnGoingChallenges()
    }
}