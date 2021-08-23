package com.example.candy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.candy.adapter.ProblemRecyclerAdapter
import com.example.candy.databinding.ActivityMarkingProblemBinding

class MarkingProblemActivity : AppCompatActivity() {

    private val Tag = "MarkingProblemActivity"
    private var mBinding: ActivityMarkingProblemBinding? = null
    private val binding get() = mBinding!!
    private val markingResultList = arrayListOf<String>()
    private lateinit var markingAdapter: ProblemRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMarkingProblemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.titleTV.text = "1 / 5"
        // 임시 문제 리스트
        markingResultList.add("요약")
        for(i in 1..14){
            markingResultList.add(i.toString())
        }

        markingAdapter = ProblemRecyclerAdapter(markingResultList, this)
        binding.problemRecyclerView.apply{
            setAdapter(adapter)
            layoutManager = LinearLayoutManager(this@MarkingProblemActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.problemRecyclerView.adapter = markingAdapter

        initListeners()
    }

    private fun initListeners() {
        with(binding){
            retryButton.setOnClickListener {
                finish()    // 임시
            }
            finishButton.setOnClickListener {
                finish()
            }


            prevProblemTV.setOnClickListener {
                with(markingAdapter){
                    if(selectedPosition > 0){
                        updateItemColor(selectedPosition-1)
                        recyclerItemClicked(selectedPosition)
                    }
                }
            }

            nextProblemTV.setOnClickListener {
                with(markingAdapter){
                    if(selectedPosition < markingResultList.size-1){
                        updateItemColor(selectedPosition+1)
                        recyclerItemClicked(selectedPosition)
                    }
                }
            }
        }
    }


    fun recyclerItemClicked(pos: Int){
        Log.d(Tag, "Clicked / position : $pos")

        // 요약 / 채점 결과 layout switch
        if(pos == 0){
            binding.markingSummaryLayout.root.visibility = View.VISIBLE
            binding.markingResultLayout.root.visibility = View.GONE
        }else{
            binding.markingSummaryLayout.root.visibility = View.GONE
            binding.markingResultLayout.root.visibility = View.VISIBLE
        }
    }
}