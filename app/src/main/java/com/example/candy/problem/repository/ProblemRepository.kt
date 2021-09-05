package com.example.candy.problem.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.candy.model.api.CandyApi
import com.example.candy.model.api.ProblemApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.data.*
import com.example.candy.utils.API.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProblemRepository() {
    private val retrofit = RetrofitClient.getClient(BASE_URL)
    private val api = retrofit.create(ProblemApi::class.java)

    private val _scoredScore = MutableLiveData<Int>()
    val scoredScore: LiveData<Int> = _scoredScore

    private val _problemList = MutableLiveData<List<Problem>>()
    val problemList : LiveData<List<Problem>> = _problemList

    // 틀린 문제의 problemId 를 저장
    val wrongAnswer = ArrayList<Int>()

    // 틀린 문제 데이터 받아오기
    fun getWrongProblem() : MutableList<Problem>{
        val data = ArrayList<Problem>()
        _problemList.value?.forEachIndexed { index, problem ->
            if(wrongAnswer.contains(problem.problemId)){
                data.add(problem)
            }
        }
        return data
    }

    fun initScoredScore(){
        _scoredScore.value = 0
    }

    fun addScore(score: Int){
        val currentValue:Int = _scoredScore.value!!
        _scoredScore.value = currentValue + score
    }


    fun getProblem(apiKey: String, challengeID: HashMap<String, Int>) : LiveData<ProblemList>{
        val data = MutableLiveData<ProblemList>()
        api.getProblem(apiKey,challengeID).enqueue(object : Callback<ProblemApiResponse> {
            override fun onResponse(
                call: Call<ProblemApiResponse>,
                response: Response<ProblemApiResponse>
            ) {
                val TAG: String = "ProblemRepository"
                if (response.code() == 200) {
                    Log.d(TAG, "${response.body()!!.response.problem.size}")
                    data.value = response.body()!!.response
                    _problemList.value = response.body()!!.response.problem
                }
            }

            override fun onFailure(call: Call<ProblemApiResponse>, t: Throwable) {

            }
        })

        return data
    }

    fun postProblemSolve(apiKey: String, problemSolveDto : ProblemSolveDto){
        api.sendProblemSolve(apiKey,problemSolveDto).enqueue(object : Callback<ProblemSolveResponse>{
            override fun onResponse(
                call: Call<ProblemSolveResponse>,
                response: Response<ProblemSolveResponse>
            ) {
                if(response.code() == 200){
                    _scoredScore.value = response.body()!!.response.totalScore
                }
            }

            override fun onFailure(call: Call<ProblemSolveResponse>, t: Throwable) {

            }
        })
    }
}