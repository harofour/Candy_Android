package com.example.candy.model.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.candy.model.api.ChallengeApi
import com.example.candy.model.data.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ChallengeListRepositoryImpl(
    private val challengeApi: ChallengeApi,
    private val context: Context
): ChallengeListRepository {

    // 도전 가능 챌린지 리스트 조회
    override suspend fun searchPossibleChallenge(apiKey: String, lastChallengeId: Int, size: Int): ArrayList<Challenge>? {
        // 통신코드 작성
        val request = challengeApi.getPossibleChallengeList(apiKey, lastChallengeId, size)

        if(request.isSuccessful){
            Log.d("possibleChallengeList", request.body().toString())

            return request.body()!!.response
        }
        else {
            // 실패 시
            Toast.makeText(context, "도전 가능 리스트 조회 오류", Toast.LENGTH_SHORT).show()
            return null
        }
   }

    // 챌린지 찜하기
    override suspend fun touchLikeBtn(apiKey: String, challengeId: Int, previousState: Boolean): Boolean {
        val request = challengeApi.touchLikeBtn(apiKey, challengeId)

        if(request.isSuccessful){
            Log.d("api test", "challenge like success" )

            if(previousState)
                Toast.makeText(context, "찜 목록에서 챌린지가 제거됩니다", Toast.LENGTH_SHORT ).show()
            else
                Toast.makeText(context, "찜 목록에 챌린지가 추가됩니다", Toast.LENGTH_SHORT ).show()

            return request.body()!!.isSuccess
        }
        else{
            Toast.makeText(context, "찜하기 실패", Toast.LENGTH_SHORT ).show()
            return false
        }
    }

    // 찜한 챌린지 조회
    override suspend fun searchLikeChallenge(apiKey: String, lastChallengeId: Int, size: Int): ArrayList<Challenge>? {
        // 통신코드 작성
        val request = challengeApi.getLikeChallengeList(apiKey, lastChallengeId, size)

        if(request.isSuccessful){
            Log.d("likeChallengeList", request.body().toString())

            return request.body()!!.response
        }
        else {
            // 실패 시
            Toast.makeText(context, "찜 리스트 조회 오류", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    // 완료 챌린지 조회
    override suspend fun searchCompleteChallenge(apiKey: String, lastChallengeId: Int, size: Int): ArrayList<ChallengeComplete>?{
        // 통신코드 작성
        val request = challengeApi.getCompletedList(apiKey, lastChallengeId, size)

        if(request.isSuccessful){
            Log.d("api test check", "like result" +  request.body().toString())

            return request.body()!!.response
        }
        else {
            // 실패 시
            Toast.makeText(context, "완료 리스트 조회 오류", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    // 챌린지 세부 정보 가져오기
    override suspend fun searchChallengeDetail(apiKey: String, challengeId: Int): ChallengeDetail? {

        val request = challengeApi.getChallengeDetail(apiKey, challengeId)

        if(request.isSuccessful){
            Log.d("api test check", "챌린지 세부 정보 성공 get challenge detail success")
            //Toast.makeText(context, "챌린지 세부 정보 가져오기 성공", Toast.LENGTH_SHORT).show()
            return  request.body()!!.response
        }
        else{
            Log.d("api test check", "get challenge detail fail")
            // 실패 시
            Toast.makeText(context, "챌린지 세부 정보 오류", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    // 학부모 캔디 개수 조회
    override fun getParentCandyAmount(apiKey: String): Single<CandyResponse2> =
        challengeApi.getParentCandyAmount(apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    // 챌린지에 캔디 할당
    override fun assignCandy(
        apiKey: String,
        challengeId: Int,
        candyCnt: Int,
        parrentPassword: String
    ): Single<CandyAssignResponse> =
        challengeApi.assignCandy(apiKey, CandyAssignBody(candyCnt, challengeId, parrentPassword))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    // 강의 영상 url 조회
    override fun loadVideo(apiKey: String, challengeId: Int, lectrueId: Int): Single<Lecture> =
            challengeApi.loadVideoRx(apiKey, LectureCheckRequestBody(challengeId, lectrueId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())



}