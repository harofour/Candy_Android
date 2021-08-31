package com.example.candy.myPage.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.R
import com.example.candy.databinding.ItemStudentCandyBinding
import com.example.candy.model.data.History

class HistoryAdapter() : ListAdapter<History,HistoryAdapter.ViewHolder>(HistoryDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemStudentCandyBinding>(layoutInflater,viewType,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_student_candy
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }



    inner class ViewHolder(private val binding : ItemStudentCandyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(history : History){
            if(history.eventType=="캔디 충전" || history.eventType=="캔디 획득"){
                binding.amountTv.setTextColor(Color.parseColor("#0054FF"))
            }else{
                binding.amountTv.setTextColor(Color.parseColor("#F15F5F"))
            }
            binding.history = history
        }
    }

    companion object HistoryDiffUtil : DiffUtil.ItemCallback<History>(){
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem==newItem
        }
    }
}