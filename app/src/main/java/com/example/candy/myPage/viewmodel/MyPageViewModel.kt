package com.example.candy.myPage.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.candy.model.data.History
import com.example.candy.model.data.User
import com.example.candy.model.data.UserInfo
import com.example.candy.myPage.repository.MyPageRepository
import com.example.candy.utils.RESPONSE_STATE

class MyPageViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MyPageRepository()


    fun getParentHistories() : LiveData<List<History>>{
        return repository.getParentHistories()
    }

    fun getStudentHistories() : LiveData<List<History>>{
        return repository.getStudentHistories()
    }

    fun getAPIHistoryData(apiKey: String,identity : String, category: String,lastId: String,size:String){
        repository.getAPIHistoryData(apiKey, identity, category, lastId, size)
    }

    fun getUserInfo(): User {
        return repository.getUserInfo()
    }

    fun getAPIUserInfo(apiKey: String): LiveData<UserInfo> {
        return repository.getAPIUserInfo(apiKey)
    }

    fun updateUserInfo(
        apiKey: String,
        userData: HashMap<String, Any>,
        completion: (RESPONSE_STATE) -> Unit
    ) {
        return repository.updateAPIUserInfoChange(apiKey, userData, completion)
    }

    fun changePw(
        apiKey: String,
        userData: HashMap<String, Any>,
        completion: (RESPONSE_STATE) -> Unit
    ) {
        return repository.changePw(apiKey, userData, completion)
    }
}