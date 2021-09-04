package com.example.candy.model.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.candy.myPage.MyPageRepository
import kotlinx.coroutines.withTimeout

class SharedViewModel : ViewModel() {
    companion object {
        private var instance: SharedViewModel? = null
        fun getInstance() = instance ?: synchronized(SharedViewModel::class.java) {
            instance ?: SharedViewModel().also { instance = it }
        }
    }

    private val repository = MyPageRepository()

    private var userPw: String? = null

    /**
     * 학부모 캔디 관리하는 함수
     */
    fun getCandyParent(): LiveData<String> {
        return repository.getCandyParent()
    }

    fun getAPICandyParent(apiKey: String) {
        repository.getAPICandyParent(apiKey)
    }

    fun updateCandyParent(apiKey: String, chargeCandy: HashMap<String, Int>) {
        return repository.updateCandyParent(apiKey, chargeCandy)
    }

    /**
     * 학생 캔디 관리하는 함수
     */
    fun getCandyStudent(): LiveData<String> {
        return repository.getCandyStudent()
    }

    fun getAPICandyStudent(apiKey: String) {
        repository.getAPICandyStudent(apiKey)
    }

    fun updateCandyStudent(apiKey: String, chargeCandy: HashMap<String, Int>) {
        repository.updateCandyStudent(apiKey, chargeCandy)
    }

    fun assignCandyToStudent(assignedCandy: Int) {
        repository.assignCandyToStudent(assignedCandy)
    }


    fun setUserPw(pw: String) {
        this.userPw = pw
    }

    fun getUserPw(): String {
        return this.userPw ?: ""
    }

    fun getAPIScoredScore(apiKey: String,challengeId : Int){
        repository.getAPIScoredScore(apiKey,challengeId)
    }

    fun getScoredScore() : LiveData<Int>{
        return repository.scoredScore
    }
}