package com.example.candy.problem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.candy.model.data.Candy
import com.example.candy.model.data.User

class ProblemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProblemRepository(application)

}