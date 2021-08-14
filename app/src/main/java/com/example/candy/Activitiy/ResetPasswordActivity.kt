package com.example.candy.Activitiy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.candy.R
import com.example.candy.databinding.ActivityFindPasswordBinding
import com.example.candy.databinding.ActivityLogInBinding
import com.example.candy.utils.Util

class FindPasswordActivity : AppCompatActivity() {
    val Tag = "FindPasswordActivity"
    private var mBinding: ActivityFindPasswordBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)
        setSupportActionBar(findViewById(R.id.toolbar))

        with(supportActionBar!!){
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "비밀번호 찾기"
        }

        mBinding = ActivityFindPasswordBinding.inflate(layoutInflater)

        initListeners()

    }

    fun initListeners(){
        with(binding){
            sendEmailAuthBtn.setOnClickListener {
//                sendEmailAuthBtn.isClickable = false
                val times = 5
                Util.timer(
                    times = times,
                    timeMillis = 1000,
                    action = {
                        Log.d(Tag, "action / ${timeToString(times - it)} / ${sendEmailAuthBtn.isClickable}")
                        runOnUiThread{
                            sendEmailAuthBtn.alpha = 0.5f
                        }
                    },
                    finished= {
                        sendEmailAuthBtn.isClickable = true

                        runOnUiThread{
                            sendEmailAuthBtn.alpha = 1f
                        }
                    }
                )
            }
        }

    }
    private fun timeToString(time: Int) : String {
        var min = ""
        var sec = ""
        if(time>120){
            min = "2"
            sec = "${time-120}"
        }else if(time>60){
            min = "1"
            sec = "${time-60}"
        }else{
            min = "0"
            sec = "$time"
        }
        return "$min : $sec"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}