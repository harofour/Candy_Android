package com.example.candy.problem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.candy.problem.repository.ProblemRepository

class ProblemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProblemRepository(application)

}