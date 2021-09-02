package com.example.candy.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.candy.model.data.OnGoingChallenge
import java.util.*

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

    fun getOnGoingChallengeLiveData(): LiveData<ArrayList<OnGoingChallenge>> {
        return repository.getOnGoingChallengeLiveData()
    }

    fun clearLiveData() {
        repository.clearLiveData()
    }

    fun loadData(size: Int, category: String) {
        return repository.loadData(size, category)
    }

    fun getChallenge(position: Int): OnGoingChallenge {
        return repository.getChallenge(position)
    }

    fun removeOnGoingChallenge(onGoingChallenge: OnGoingChallenge) {
        repository.removeOnGoingChallenge(onGoingChallenge)
    }

    suspend fun cancelAssignedCandy(reqData: HashMap<String, Any>): Boolean {
        return repository.cancelAssignedCandy(reqData)
    }
}