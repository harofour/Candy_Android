package com.example.candy.challenge.viewmodel

import android.util.Log
import com.example.candy.data.ApiAnyResponse
import com.example.candy.model.api.CandyApi
import com.example.candy.model.api.ChallengeApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.data.OnGoingChallenge
import com.example.candy.utils.API
import com.example.candy.utils.CurrentUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LectureRepository {
    val candyApi = RetrofitClient.getClient(API.BASE_URL).create(CandyApi::class.java)
    val challengeApi = RetrofitClient.getClient(API.BASE_URL).create(ChallengeApi::class.java)
    val Tag = "LectureRepository"

    fun completeLecture(challenge: OnGoingChallenge): Boolean {
        var isCompleted = false
        CoroutineScope(Dispatchers.IO).launch {
            // 챌린지 수강 완료
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                val data = HashMap<String, Int>()
                data.put("challengeId", challenge.challengeId)
                val call = candyApi.attainCandy(CurrentUser.userToken!!, data)
                Log.d("Tag", "${CurrentUser.userToken} / $data")

                call.enqueue(object : Callback<ApiAnyResponse> {
                    override fun onResponse(
                        call: Call<ApiAnyResponse>,
                        response: Response<ApiAnyResponse>
                    ) {
                        if (response.isSuccessful) {
                            // 챌린지 수강 완료
                            isCompleted = true
                            Log.d(Tag, "completeLecture / 캔디 획득 성공")
                        } else {
                            Log.d(Tag, "completeLecture / 캔디 획득 실패")
                        }
                    }

                    override fun onFailure(call: Call<ApiAnyResponse>, t: Throwable) {
                        Log.d(Tag, "completeLecture / onFailure")
                    }
                })
            }
        }
        return isCompleted
    }

    fun getThombnailImage() {
    }

    suspend fun loadVideo(challengeId: Int, lectureId: Int): String? =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val reqData = HashMap<String, Int>()
            reqData.put("challengeId", challengeId)
            reqData.put("lectureId", lectureId)
            val response = challengeApi.loadVideo(CurrentUser.userToken!!, reqData)
            if (response.isSuccessful) {
                Log.d(Tag, "loadVideo() success/ ${response.body()}")
                response.body()!!.lecturesUrl[0]
            } else {
                Log.d(Tag, "loadVideo() fail/ ${response.message()}")
                null
            }
        }
}