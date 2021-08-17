package com.example.candy.activity

import android.os.Bundle
import android.util.Log
import com.example.candy.Activitiy.BaseActivity
import com.example.candy.R
import com.example.candy.data.ApiStringResponse
import com.example.candy.databinding.ActivityFindIdBinding
import com.example.candy.retrofit.RetrofitManager
import com.example.candy.utils.REQUEST_TYPE
import com.example.candy.utils.RESPONSE_STATE
import com.example.candy.utils.Util
import com.google.gson.Gson

class FindIdActivity : BaseActivity() {
    val Tag = "FindIdActivity"
    private var mBinding: ActivityFindIdBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFindIdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))



        with(supportActionBar!!) {
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "아이디 찾기"
        }

        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            findBtn.setOnClickListener {
                findId()
            }
        }
    }

    private fun findId() {
        val data = HashMap<String, Any>()
        data.put("name",binding.nameET.text.toString())

        RetrofitManager.instance.requestString(data, REQUEST_TYPE.FIND_EMAIL){ responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.SUCCESS->{
                    val result = Gson().fromJson(responseBody, ApiStringResponse::class.java)
                    val name = result.response
                    Log.d(Tag, name)

                    Util.toast(applicationContext, "아이디는 '$name' 입니다")
                }
                RESPONSE_STATE.FAILURE->{
                    Util.toast(applicationContext, "아이디를 찾기를 실패했습니다.")
                }
            }
        }
    }
}