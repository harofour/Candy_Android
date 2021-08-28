package com.example.candy.model.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.candy.myPage.MyPageRepository
import com.example.candy.utils.RESPONSE_STATE

class SharedViewModel : ViewModel() {
    private val repository = MyPageRepository()

    private var userPw: String? = null

    private val _candyParent = MutableLiveData<String>()
    val candyParent: LiveData<String> = _candyParent

    private val _candyStudent = MutableLiveData<String>()
    val candyStudent: LiveData<String> = _candyStudent

    /**
     * 학부모 캔디 관리하는 함수
     */
    fun getAPICandyParent(apiKey: String,completion: (RESPONSE_STATE,String?) -> Unit){
        repository.getAPICandyParent(apiKey,completion)
    }

    fun updateCandyParent(apiKey: String, chargeCandy: HashMap<String, Int>,completion: (RESPONSE_STATE) -> Unit) {
        return repository.updateCandyParent(apiKey, chargeCandy,completion)
    }

    fun setCandyParentInApp(candy : String){
        _candyParent.value = candy
    }

    fun updateCandyParentInApp(candy: Int){
        _candyParent.value = (_candyParent.value!!.toInt() + candy).toString()
    }


    /**
     * 학생 캔디 관리하는 함수
     */
    fun getAPICandyStudent(apiKey: String,completion: (RESPONSE_STATE,String?) -> Unit){
        repository.getAPICandyStudent(apiKey,completion)
    }

    fun updateCandyStudent(apiKey: String, withDrawCandy: HashMap<String, Int>,completion: (RESPONSE_STATE) -> Unit) {
        // TODO:: 학생 캔디 인출하는 함수 repository에 만들어지면 연결
    }

    fun setCandyStudentInApp(candy : String){
        _candyStudent.value = candy
    }

    fun updateCandyStudentInApp(candy: Int){
        _candyStudent.value = (_candyStudent.value!!.toInt() + candy).toString()
    }


    fun setUserPw(pw : String){
        this.userPw = pw
    }

    fun getUserPw() : String{
        return this.userPw ?: ""
    }
}