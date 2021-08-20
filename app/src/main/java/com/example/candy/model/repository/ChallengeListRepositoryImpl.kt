package com.example.candy.model.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.candy.model.api.ChallengeApi
import com.example.candy.model.data.Challenge

class ChallengeListRepositoryImpl(
    private val challengeApi: ChallengeApi,
    private val context: Context
): ChallengeListRepository {


    override suspend fun searchPossibleChallenge(apiKey: String, lastChallengeId: Int, size: Int): ArrayList<Challenge>? {
        // 통신코드 작성
        val request = challengeApi.getPossibleChallengeList(apiKey, lastChallengeId, size)

        if(request.isSuccessful){
            Log.d("possibleChallengeList", request.body().toString())

            return request.body()!!.response
        }
        else {
            // 실패 시
            Toast.makeText(context, "통신에러", Toast.LENGTH_SHORT).show()
            return null
        }
   }

}