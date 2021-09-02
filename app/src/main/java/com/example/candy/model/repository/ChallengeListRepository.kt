package com.example.candy.model.repository

import com.example.candy.model.data.*
import io.reactivex.Single

interface ChallengeListRepository {

    suspend fun searchPossibleChallenge(apiKey: String, lastChallengeId: Int, size: Int): ArrayList<Challenge>?

    suspend fun touchLikeBtn(apiKey: String, challengeId: Int, previousState: Boolean): Boolean

    suspend fun searchLikeChallenge(apiKey: String, lastChallengeId: Int, size: Int): ArrayList<Challenge>?

    suspend fun searchChallengeDetail(apiKey: String, challengeId: Int): ChallengeDetail?

    fun getParentCandyAmount(apiKey: String): Single<CandyResponse2>

    fun assignCandy(apiKey: String, challengeId: Int, candyCnt: Int, parrentPassword: String): Single<CandyAssignResponse>
}