package com.example.candy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.candy.R
import com.example.candy.databinding.ActivityProblemBinding
import com.example.candy.problem.viewmodel.ProblemViewModel

class ProblemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProblemBinding
    private val problemViewModel : ProblemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProblemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        problemViewModel.challengeId = intent.getIntExtra("ChallengeId",0)
    }
}