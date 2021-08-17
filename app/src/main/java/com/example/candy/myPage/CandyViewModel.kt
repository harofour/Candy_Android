package com.example.candy.myPage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.candy.data.Candy
import com.example.candy.data.User

class CandyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CandyRepository(application)


    fun getUserInfo() : User {
        return repository.getUserInfo()
    }

    fun getAPICandyStudent(){
        return repository.getAPICandyStudent()
    }

    fun getCandyStudent() : LiveData<Candy>{
        return repository.getCandyStudent()
    }
}