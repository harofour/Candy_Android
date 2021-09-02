package com.example.candy.problem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.candy.model.data.ProblemList
import com.example.candy.problem.repository.ProblemRepository

class ProblemViewModel() : ViewModel() {
    private val repository = ProblemRepository()
    var challengeId : Int? = null


    fun getProblem(apiKey: String,challengeId: HashMap<String,Int>) : LiveData<ProblemList> {
        return repository.getProblem(apiKey,challengeId)
    }

}