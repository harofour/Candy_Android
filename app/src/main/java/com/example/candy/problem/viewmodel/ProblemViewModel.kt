package com.example.candy.problem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.candy.model.data.Problem
import com.example.candy.model.data.ProblemList
import com.example.candy.model.data.ProblemSolveDto
import com.example.candy.problem.repository.ProblemRepository

class ProblemViewModel() : ViewModel() {
    private val repository = ProblemRepository()
    var challengeId : Int? = null

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int> = _position

    // 총 문제 수와 틀린 문제는 무엇인지와 틀린 문제 데이터가 필요하다.

    fun getWrongProblem() : MutableList<Problem>{
        return repository.getWrongProblem()
    }

    fun getProblemSize() : Int?{
        return repository.problemList.value?.size
    }

    fun addWrongAnswer(problemId: Int){
        repository.wrongAnswer.add(problemId)
    }

    fun getWrongAnswerSize() : Int{
        return repository.wrongAnswer.size
    }

    fun getRightAnswerSize() : Int?{
        return repository.problemList.value?.size?.minus(repository.wrongAnswer.size)
    }

    fun getScoredScore() : Int{
        return repository.scoredScore.value ?: 0
    }

    fun initScoredScore(){
        repository.initScoredScore()
    }

    fun addScore(score: Int){
        repository.addScore(score)
    }

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

    fun postProblemSolve(apiKey: String, problemSolveDto : ProblemSolveDto){
        repository.postProblemSolve(apiKey, problemSolveDto)
    }

}