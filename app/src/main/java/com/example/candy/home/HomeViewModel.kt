package com.example.candy.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.candy.model.data.Challenge

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HomeRepository()

    fun getCategories(): LiveData<ArrayList<String>> {
        return repository.getCategories()
    }

    fun getOnGoingChallenges(): LiveData<ArrayList<Challenge>> {
        return repository.getOnGoingChallenges()
    }

    fun sortChallengeByCategory(position: Int) {
        repository.sortChallengeByCategory(position)
    }

    fun getChallenge(position: Int): Challenge {
        return repository.getChallenge(position)
    }
}