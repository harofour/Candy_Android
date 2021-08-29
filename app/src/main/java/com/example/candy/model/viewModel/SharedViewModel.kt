package com.example.candy.model.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.candy.myPage.MyPageRepository
import com.example.candy.utils.RESPONSE_STATE

class SharedViewModel : ViewModel() {
    private val repository = MyPageRepository()

    private var userPw: String? = null

    /**
     * 학부모 캔디 관리하는 함수
     */
    fun getCandyParent(): LiveData<String> {
        return repository.getCandyParent()
    }

    fun getAPICandyParent(apiKey: String){
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

    fun getAPICandyStudent(apiKey: String){
        repository.getAPICandyStudent(apiKey)
    }

    fun updateCandyStudent(apiKey: String, withDrawCandy: HashMap<String, Int>) {
        // TODO:: 학생 캔디 인출하는 함수 repository에 만들어지면 연결
    }


    fun setUserPw(pw : String){
        this.userPw = pw
    }

    fun getUserPw() : String{
        return this.userPw ?: ""
    }
}