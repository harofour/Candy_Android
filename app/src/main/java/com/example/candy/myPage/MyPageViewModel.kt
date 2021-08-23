package com.example.candy.myPage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.candy.model.data.Candy
import com.example.candy.model.data.User
import com.example.candy.model.data.UserInfo
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.RESPONSE_STATE

class MyPageViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MyPageRepository(application)


    fun getUserInfo() : User {
        return repository.getUserInfo()
    }

    fun getAPIUserInfo(apiKey: String) : LiveData<UserInfo>{
        return repository.getAPIUserInfo(apiKey)
    }

    fun getAPICandyStudent(apiKey :String){
        return repository.getAPICandyStudent(apiKey)
    }

    fun getAPICandyParent(apiKey: String){
        return repository.getAPICandyParent(apiKey)
    }

    fun getCandyStudent() : LiveData<Candy>{
        return repository.getCandyStudent()
    }

    fun getCandyParent() : LiveData<Candy>{
        return repository.getCandyParent()
    }

    fun updateCandyParent(apiKey: String,chargeCandy : HashMap<String,Int>){
        return repository.updateCandyParent(apiKey,chargeCandy)
    }

    fun updateUserInfo(apiKey: String,userData : HashMap<String,Any>,completion: (RESPONSE_STATE)->Unit){
        return repository.updateAPIUserInfoChange(apiKey,userData,completion)
    }
}