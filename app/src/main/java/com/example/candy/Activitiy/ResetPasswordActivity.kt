package com.example.candy.Activitiy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.candy.R
import com.example.candy.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : BaseActivity() {
    val Tag = "ResetPasswordActivity"
    private var mBinding: ActivityResetPasswordBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)
        setSupportActionBar(findViewById(R.id.toolbar))

        with(supportActionBar!!){
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "비밀번호 재설정"
        }

        mBinding = ActivityResetPasswordBinding.inflate(layoutInflater)

        initListeners()

    }

    fun initListeners(){
        with(binding){

        }
    }
}