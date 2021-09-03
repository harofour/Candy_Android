package com.example.candy.problem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.candy.model.data.ProblemList
import com.example.candy.problem.repository.ProblemRepository

class ProblemViewModel() : ViewModel() {
    private val repository = ProblemRepository()
    var challengeId : Int? = null

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int> = _position


    fun getProblem(apiKey: String,challengeId: HashMap<String,Int>) : LiveData<ProblemList> {
        return repository.getProblem(apiKey,challengeId)
    }

    fun updatePosition(pos : Int){
        _position.value = pos
    }

    fun addPosition(){
        val currentValue:Int = _position.value!!
        _position.value = currentValue + 1
    }

    fun minusPosition(){
        val currentValue:Int = _position.value!!
        _position.value = currentValue - 1
    }

}