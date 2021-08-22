package com.example.candy.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.R
import com.example.candy.databinding.ItemProblemRecyclerviewBinding

class ProblemRecyclerAdapter(
    private var dataSet: List<String>
): RecyclerView.Adapter<ProblemRecyclerAdapter.MyViewHolder>(){
    val Tag = "ProblemRecyclerAdapter"
    var selectedPosition = 0
    private val selectedColor = "#FBCFCF"
    private val unSelectedColor = "#C4C4C4"

    class MyViewHolder(val binding: ItemProblemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {  // 어떤 view 에서 생성된 바인딩 인지 root에 담고 있다
        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val view =LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_problem_recyclerview, viewGroup, false)

        return MyViewHolder(ItemProblemRecyclerviewBinding.bind(view))
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        myViewHolder.binding.problemPosTextView.text = dataSet[position]

        if(selectedPosition == position){
            myViewHolder.binding.problemPosTextView.setBackgroundColor(Color.parseColor(selectedColor))
        }else{
            myViewHolder.binding.problemPosTextView.setBackgroundColor(Color.parseColor(unSelectedColor))
        }

        // 문제 클릭 시
        myViewHolder.binding.root.setOnClickListener {
            Log.d(Tag, "Problem ${position+1} clicked")
            this.notifyItemChanged(selectedPosition)
            selectedPosition = position
            this.notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}