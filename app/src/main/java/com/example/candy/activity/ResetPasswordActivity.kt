package com.example.candy.activity

import android.content.Intent
import android.os.Bundle
import com.example.candy.Activitiy.BaseActivity
import com.example.candy.R
import com.example.candy.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : BaseActivity() {
    val Tag = "FindPasswordActivity"
    private var mBinding: ActivityResetPasswordBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)
        setSupportActionBar(findViewById(R.id.toolbar))

        with(supportActionBar!!) {
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "비밀번호 찾기"
        }

        mBinding = ActivityResetPasswordBinding.inflate(layoutInflater)

        initListeners()
    }

    fun initListeners() {
        with(binding) {

            finishButton.setOnClickListener {
                val intent = Intent(applicationContext,LogInActivity::class.java)
                startActivity(intent)
            }
        }
    }
}