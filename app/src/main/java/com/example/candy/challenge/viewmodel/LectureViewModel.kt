package com.example.candy.challenge.viewmodel

import androidx.lifecycle.ViewModel
import com.example.candy.model.data.OnGoingChallenge

class LectureViewModel : ViewModel() {
    private val repository = LectureRepository()

    fun completeLecture(challenge: OnGoingChallenge): Boolean {
        return repository.completeLecture(challenge)
    }

    fun getThombnailImage() {
        repository.getThombnailImage()
        //return smoething
    }

    suspend fun loadVideo(challengeId: Int, lectureId: Int): String? {
        return repository.loadVideo(challengeId, lectureId)
    }

}