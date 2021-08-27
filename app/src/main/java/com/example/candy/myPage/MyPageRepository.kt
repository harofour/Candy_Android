package com.example.candy.myPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.candy.model.api.CandyApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.api.UserInfoApi
import com.example.candy.model.data.*
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.RESPONSE_STATE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageRepository() {
    val TAG: String = "로그"
    private val retrofit = RetrofitClient.getClient(BASE_URL)
    private val apiCandy = retrofit.create(CandyApi::class.java)
    private val apiUserInfo = retrofit.create(UserInfoApi::class.java)


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
    fun getAPICandyParent(apiKey: String,completion: (RESPONSE_STATE,String?) -> Unit) {
        apiCandy.getCandyParent(apiKey).enqueue(object : Callback<CandyResponse> {
            override fun onResponse(call: Call<CandyResponse>, response: Response<CandyResponse>) {
                if(response.code() == 200){
                    completion(RESPONSE_STATE.SUCCESS,response.body()!!.candy.candy)
                }else{
                    completion(RESPONSE_STATE.FAILURE,null)
                }
            }

            override fun onFailure(call: Call<CandyResponse>, t: Throwable) {
                completion(RESPONSE_STATE.FAILURE,null)
            }
        })
    }

    fun updateCandyParent(apiKey: String, chargeCandy: HashMap<String, Int>,completion: (RESPONSE_STATE) -> Unit) {
        apiCandy.chargeCandy(apiKey, chargeCandy).enqueue(object : Callback<chargeCandyResponse> {
            override fun onResponse(
                call: Call<chargeCandyResponse>,
                response: Response<chargeCandyResponse>
            ) {
                Log.d(TAG, "MyPageRepository -------- ${response.code()}")
                if (response.code() == 200 && response.body()!!.success) {
                    completion(RESPONSE_STATE.SUCCESS)
                }else{
                    completion(RESPONSE_STATE.FAILURE)
                }
            }

            override fun onFailure(call: Call<chargeCandyResponse>, t: Throwable) {
                completion(RESPONSE_STATE.FAILURE)
            }
        })
    }

    /**
     * 학생 캔디 관리하는 함수
     */
    fun getAPICandyStudent(apiKey: String,completion: (RESPONSE_STATE, String?) -> Unit) {
        apiCandy.getCandyStudent(apiKey).enqueue(object : Callback<CandyResponse> {
            override fun onResponse(call: Call<CandyResponse>, response: Response<CandyResponse>) {
                if(response.code() == 200){
                    completion(RESPONSE_STATE.SUCCESS,response.body()!!.candy.candy)
                }else{
                    completion(RESPONSE_STATE.FAILURE,null)
                }
            }
            override fun onFailure(call: Call<CandyResponse>, t: Throwable) {
                completion(RESPONSE_STATE.FAILURE,null)
            }
        })
    }

    // TODO:: 학생 인출 함수 만들기







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
}