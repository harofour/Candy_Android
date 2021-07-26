package com.example.candy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.candy.databinding.ActivityLogInBinding


class LogInActivity : AppCompatActivity() {
    private var mBinding: ActivityLogInBinding? = null
    private val binding get() = mBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners(){
        with(binding){
            loginBtn.setOnClickListener {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            findIdBtn.setOnClickListener {
                val intent = Intent(applicationContext, FindIdActivity::class.java)
                startActivity(intent)
            }
            findPwdBtn.setOnClickListener {
                val intent = Intent(applicationContext, FindPasswordActivity::class.java)
                startActivity(intent)
            }
            signupBtn.setOnClickListener {
                val intent = Intent(applicationContext, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
}