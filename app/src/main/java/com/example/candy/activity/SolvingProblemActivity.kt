package com.example.candy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.candy.adapter.ProblemRecyclerAdapter
import com.example.candy.databinding.ActivitySolvingProblemBinding

class SolvingProblemActivity : AppCompatActivity() {
    private var mBinding: ActivitySolvingProblemBinding? = null
    private val binding get() = mBinding!!
    private val problemList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySolvingProblemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.currentProblemTV.text = "1 / 5"
        // 임시 문제 리스트
        for(i in 1..15){
            problemList.add(i.toString())
        }

        val adapter = ProblemRecyclerAdapter(problemList)
        binding.problemRecyclerView.apply{
            setAdapter(adapter)
            layoutManager = LinearLayoutManager(this@SolvingProblemActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.problemRecyclerView.adapter = adapter
    }
}