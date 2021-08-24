package com.example.candy.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.candy.adapter.ProblemRecyclerAdapter
import com.example.candy.databinding.ActivitySolvingProblemBinding

class SolvingProblemActivity : AppCompatActivity() {
    private val Tag = "SolvingProblemActivity"
    private var mBinding: ActivitySolvingProblemBinding? = null
    private val binding get() = mBinding!!
    private val problemList = arrayListOf<String>()
    lateinit var problemAdapter: ProblemRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySolvingProblemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.titleTV.text = "1 / 5"
        // 임시 문제 리스트
        for(i in 1..15){
            problemList.add(i.toString())
        }

        problemAdapter = ProblemRecyclerAdapter(problemList, this)
        binding.problemRecyclerView.apply{
            adapter = problemAdapter
            layoutManager = LinearLayoutManager(this@SolvingProblemActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.problemRecyclerView.adapter = problemAdapter

        initListeners()
    }

    private fun initListeners(){
        with(binding){
            markButton.setOnClickListener {
                Log.d(Tag, "markButton Clicked")
                val activityIntent = Intent(applicationContext, MarkingProblemActivity::class.java)
                startActivity(activityIntent)
            }
            finishButton.setOnClickListener {
                Log.d(Tag, "finishButton Clicked")
                finish()
            }

            prevProblemTV.setOnClickListener {
                with(problemAdapter){
                    if(selectedPosition > 0){
                        updateItemColor(selectedPosition-1)
                        recyclerItemClicked(selectedPosition)
                    }
                }
            }

            nextProblemTV.setOnClickListener {
                with(problemAdapter){
                    if(selectedPosition < problemList.size-1){
                        updateItemColor(selectedPosition+1)
                        recyclerItemClicked(selectedPosition)
                    }
                }
            }
        }
    }


    fun recyclerItemClicked(pos: Int){
        Log.d(Tag, "Clicked / position : $pos")

    }
}