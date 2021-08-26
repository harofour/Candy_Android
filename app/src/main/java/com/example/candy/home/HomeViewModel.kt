package com.example.candy.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val repository = HomeRepository()

    fun getCategories(): LiveData<ArrayList<String>> {
        return repository.getCategories()
    }
}