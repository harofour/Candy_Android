package com.example.candy.myPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.candy.model.api.CandyApi
import com.example.candy.model.api.ProblemApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.api.UserInfoApi
import com.example.candy.model.data.*
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.RESPONSE_STATE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class MyPageRepository() {
    private val retrofit = RetrofitClient.getClient(BASE_URL)
    private val apiCandy = retrofit.create(CandyApi::class.java)
    private val apiUserInfo = retrofit.create(UserInfoApi::class.java)
    private val apiProblem = retrofit.create(ProblemApi::class.java)

    var parentCandy = MutableLiveData<String>()
    var studentCandy = MutableLiveData<String>()
    var historyParentData = MutableLiveData<List<History>>()
    var historyStudentData = MutableLiveData<List<History>>()

    var scoredScore = MutableLiveData<Int>()


    /**
     * 유저 정보 관련 함수
     */
    fun getUserInfo(): User {
        return CurrentUser.userInfo!!
    }

    fun getAPIUserInfo(apiKey: String): LiveData<UserInfo> {
        val data = MutableLiveData<UserInfo>()
        apiUserInfo.getUserInfo(apiKey).enqueue(object : Callback<UserInfoResponse> {
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                if (response.code() == 200) {
                    var userInfo = response.body()?.response
                    userInfo?.birth!!.replace("-", "").also { userInfo.birth = it }
                    userInfo.phone.replace("-", "").also { userInfo.phone = it }
                    data.value = userInfo!!
                }
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
            }
        })

        return data
    }

    fun updateAPIUserInfoChange(
        apiKey: String,
        data: HashMap<String, Any>,
        completion: (RESPONSE_STATE) -> Unit
    ) {
        apiUserInfo.updateUserInfo(apiKey, data).enqueue(object : Callback<UserInfoResponse> {
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                if (response.code() == 200 && response.body()?.success!!) {
                    completion(RESPONSE_STATE.SUCCESS)
                } else
                    completion(RESPONSE_STATE.FAILURE)
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                completion(RESPONSE_STATE.FAILURE)
            }
        })
    }


    /**
     * 학부모 캔디 관리하는 함수
     */
    fun getCandyParent(): LiveData<String> {
        return parentCandy
    }

    fun getAPICandyParent(apiKey: String) {
        apiCandy.getCandyParent(apiKey).enqueue(object : Callback<CandyResponse> {
            override fun onResponse(call: Call<CandyResponse>, response: Response<CandyResponse>) {
                if (response.code() == 200) {
                    parentCandy.value = response.body()!!.candy.candy
                }
            }

            override fun onFailure(call: Call<CandyResponse>, t: Throwable) {

            }
        })
    }

    fun updateCandyParent(apiKey: String, chargeCandy: HashMap<String, Int>) {
        apiCandy.chargeCandy(apiKey, chargeCandy).enqueue(object : Callback<ChargeCandyResponse> {
            override fun onResponse(
                call: Call<ChargeCandyResponse>,
                response: Response<ChargeCandyResponse>
            ) {
                if (response.code() == 200 && response.body()!!.success) {
                    parentCandy.value =
                        (parentCandy.value!!.toInt() + chargeCandy["amount"]!!).toString()
                }
            }

            override fun onFailure(call: Call<ChargeCandyResponse>, t: Throwable) {

            }
        })
    }


    /**
     * 학생 캔디 관리하는 함수
     */
    fun getCandyStudent(): LiveData<String> {
        return studentCandy
    }

    fun getAPICandyStudent(apiKey: String) {
        apiCandy.getCandyStudent(apiKey).enqueue(object : Callback<CandyResponse> {
            override fun onResponse(call: Call<CandyResponse>, response: Response<CandyResponse>) {
                if (response.code() == 200) {
                    studentCandy.value = response.body()!!.candy.candy
                }
            }

            override fun onFailure(call: Call<CandyResponse>, t: Throwable) {

            }
        })
    }

    fun updateCandyStudent(apiKey: String, chargeCandy: HashMap<String, Int>) {
        apiCandy.withdrawCandy(apiKey, chargeCandy).enqueue(object : Callback<ChargeCandyResponse> {
            override fun onResponse(
                call: Call<ChargeCandyResponse>,
                response: Response<ChargeCandyResponse>
            ) {
                if (response.code() == 200 && response.body()!!.success) {
                    studentCandy.value =
                        (studentCandy.value!!.toInt() - chargeCandy["amount"]!!).toString()
                }
            }

            override fun onFailure(call: Call<ChargeCandyResponse>, t: Throwable) {
            }
        })
    }

    /**
     * 캔디 내역 불러오는 함수
     */
    fun getParentHistories(): LiveData<List<History>> {
        return historyParentData
    }
    fun getStudentHistories(): LiveData<List<History>> {
        return historyStudentData
    }

    fun getAPIHistoryData(
        apiKey: String,
        identity: String,
        category: String,
        lastId: String,
        size: String
    ) {
        apiCandy.getCandyHistory(apiKey, identity, category, lastId, size)
            .enqueue(object : Callback<HistoryResponse> {
                override fun onResponse(
                    call: Call<HistoryResponse>,
                    response: Response<HistoryResponse>
                ) {
                    if (response.code() == 200) {
                        if(identity == "parent"){
                            val data = response.body()!!.response
                            data.forEachIndexed { index, history ->
                                data[index].createDate = changeDateFormat(history.createDate)
                                if(data[index].eventType == "CHARGE"){
                                    data[index].eventType = "캔디 충전"
                                    data[index].amount = "+${history.amount}C"
                                }else{
                                    data[index].eventType = "캔디 배정"
                                    data[index].amount = "-${history.amount}C"
                                }
                            }
                            historyParentData.value = data
                        }else{
                            val data = response.body()!!.response
                            data.forEachIndexed { index, history ->
                                data[index].createDate = changeDateFormat(history.createDate)
                                if(data[index].eventType == "ATTAIN"){
                                    data[index].eventType = "캔디 획득"
                                    data[index].amount = "+${history.amount}C"
                                }else{
                                    data[index].eventType = "캔디 인출"
                                    data[index].amount = "-${history.amount}C"
                                }
                            }
                            historyStudentData.value = data
                        }

                    }
                }

                override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {

                }
            })
    }

    fun assignCandyToStudent(assignedCandy: Int) {
        Log.d("updateCandyStudent", "before studentCandy / ${studentCandy.value}")
        studentCandy.value = (studentCandy.value!!.toInt() + assignedCandy).toString()
        Log.d("updateCandyStudent", "after studentCandy / ${studentCandy.value}")
    }



    fun changePw(apiKey: String, data: HashMap<String, Any>, completion: (RESPONSE_STATE) -> Unit) {
        apiUserInfo.changePw(apiKey, data).enqueue(object : Callback<UserChangePwResponse> {
            override fun onResponse(
                call: Call<UserChangePwResponse>,
                response: Response<UserChangePwResponse>
            ) {
                if (response.code() == 200) {
                    completion(RESPONSE_STATE.SUCCESS)
                } else {
                    completion(RESPONSE_STATE.FAILURE)
                }
            }

            override fun onFailure(call: Call<UserChangePwResponse>, t: Throwable) {
                completion(RESPONSE_STATE.FAILURE)
            }
        })
    }

    private fun changeDateFormat(str : String) : String {
        val oldFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val newFormat = SimpleDateFormat("yyyy.MM.dd (E)")
        val old = oldFormat.parse(str)
        return newFormat.format(old)
    }
    fun updateCandyStudent(assignedCandy: Int) {
        Log.d("updateCandyStudent", "before studentCandy / ${studentCandy.value}")
        studentCandy.value = (studentCandy.value!!.toInt() + assignedCandy).toString()
        Log.d("updateCandyStudent", "after studentCandy / ${studentCandy.value}")
    }

    /**
     * 챌린지 관련 함수
     */
    fun getAPIScoredScore(apiKey : String, challengeId : Int){
        apiProblem.getScoredScore(apiKey,challengeId).enqueue(object : Callback<ScoredScoreResponse>{
            override fun onResponse(
                call: Call<ScoredScoreResponse>,
                response: Response<ScoredScoreResponse>
            ) {
                if(response.code() == 200){
                    scoredScore.value = response.body()!!.response.score
                }
            }

            override fun onFailure(call: Call<ScoredScoreResponse>, t: Throwable) {
            }
        })
    }
}