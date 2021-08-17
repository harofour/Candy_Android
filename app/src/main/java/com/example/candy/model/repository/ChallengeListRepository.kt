package com.example.candy.model.repository

import com.example.candy.model.data.Challenge

interface ChallengeListRepository {

    suspend fun searchPossibleChallenge(apiKey: String): List<Challenge>?
}