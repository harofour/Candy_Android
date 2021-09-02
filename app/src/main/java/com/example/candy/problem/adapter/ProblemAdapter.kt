package com.example.candy.problem.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.R
import com.example.candy.databinding.ItemProblemRecyclerviewBinding
import com.example.candy.model.data.ProblemNumber

class ProblemAdapter(context: Context, numItemListener: IOnItemClickInterface) : RecyclerView.Adapter<ProblemAdapter.ViewHolder>() {
    private var problemSize = ArrayList<ProblemNumber>()
    private var ionItemClickInterface = numItemListener
    var selectedPosition = 0
    private val selectedColor =
        ContextCompat.getDrawable(context, R.drawable.problem_number_selected)
    private val notSelectedColor =
        ContextCompat.getDrawable(context, R.drawable.problem_number_not_selected)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProblemRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding,ionItemClickInterface)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(problemSize[position],position)
    }

    override fun getItemCount(): Int = problemSize.size

    inner class ViewHolder(
        private val binding: ItemProblemRecyclerviewBinding,
        onItemClickListener: IOnItemClickInterface
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var myOnItemClickInterface = onItemClickListener

        init{
            binding.problemPosTextView.setOnClickListener(this)
        }

        fun bind(num: ProblemNumber,cPosition: Int) {
            binding.problemPosTextView.text = num.no
            if(selectedPosition == cPosition){
                binding.problemPosTextView.background = selectedColor
            }else{
                binding.problemPosTextView.background = notSelectedColor
            }
        }

        override fun onClick(p0: View?) {
            updateItemColor(bindingAdapterPosition)
            when(p0){
                binding.problemPosTextView ->{
                    this.myOnItemClickInterface.onBtnClicked(bindingAdapterPosition)
                }
            }
        }
    }

    // 데이터
    fun setProblemSize(size: Int) {
        this.problemSize.clear()
        for (i in 1..size) {
            this.problemSize.add(ProblemNumber(i.toString(),false))
        }
        notifyDataSetChanged()
    }

    fun updateItemColor(p: Int){
        this.notifyItemChanged(selectedPosition)
        selectedPosition = p
        this.notifyItemChanged(selectedPosition)
    }
}