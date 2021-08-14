package com.example.candy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.candy.Activitiy.BaseActivity
import com.example.candy.R

class FindIdActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_id)
        setSupportActionBar(findViewById(R.id.toolbar))


        with(supportActionBar!!) {
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "아이디 찾기"
        }
    }
}