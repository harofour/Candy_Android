package com.example.candy.model.repository

import com.example.candy.model.data.Challenge

interface ChallengeListRepository {

    suspend fun searchPossibleChallenge(apiKey: String, lastChallengeId: Int, size: Int): ArrayList<Challenge>?
}