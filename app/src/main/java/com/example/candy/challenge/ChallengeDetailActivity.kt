package com.example.candy.challenge

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.candy.R
import com.example.candy.databinding.ActivityChallengeDetailBinding



class ChallengeDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityChallengeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("activity", "ChallengeDetail Activity onCreate")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_detail)

        val challengeId = intent.getIntExtra("challengeId", -1)

        Toast.makeText(this,"challengeId = ${challengeId}", Toast.LENGTH_SHORT).show()

    }


}