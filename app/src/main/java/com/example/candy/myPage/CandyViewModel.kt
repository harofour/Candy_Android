package com.example.candy.myPage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.candy.model.data.Candy
import com.example.candy.model.data.User

class CandyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CandyRepository(application)


    fun getUserInfo() : User {
        return repository.getUserInfo()
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
}