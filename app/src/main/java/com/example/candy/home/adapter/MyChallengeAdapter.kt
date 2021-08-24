package com.example.candy.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.candy.databinding.ItemHomeRecyclerviewMychallengeBinding
import com.example.candy.model.data.Challenge

class MyChallengeAdapter  : RecyclerView.Adapter<MyChallengeAdapter.ViewHolder>(){

    private var challenges: List<Challenge> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeRecyclerviewMychallengeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(challenges[position])
    }

    override fun getItemCount(): Int = challenges.size

    fun setChallenges(challenges: List<Challenge>){
        this.challenges = challenges
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemHomeRecyclerviewMychallengeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(challenge: Challenge){
            binding.tvTitle.text = challenge.title
            // TODO:: 바인딩 작성 또는 데이터바인딩 사용
        }
    }
}